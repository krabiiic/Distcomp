package com.example.lab1.service;

import com.example.lab1.dto.KafkaPostMessage;
import com.example.lab1.dto.ReactionResponseTo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class PublisherKafkaService {

    private static final Logger logger = LoggerFactory.getLogger(PublisherKafkaService.class);

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final ConcurrentHashMap<String, CompletableFuture<ReactionResponseTo>> waitingResponses = new ConcurrentHashMap<>();

    @Value("${kafka.topic.in:reaction-in-topic}")
    private String inTopic;

    public PublisherKafkaService(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public CompletableFuture<ReactionResponseTo> sendAndReceive(KafkaPostMessage message) {
        String responseKey = generateResponseKey(message);
        CompletableFuture<ReactionResponseTo> responseFuture = new CompletableFuture<>();
        waitingResponses.put(responseKey, responseFuture);

        try {
            String jsonMessage = objectMapper.writeValueAsString(message);
            logger.info("Sending message: {}", jsonMessage);

            kafkaTemplate.send(inTopic, String.valueOf(message.getId()), jsonMessage)
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            waitingResponses.remove(responseKey);
                            responseFuture.completeExceptionally(ex);
                            logger.error("Error sending message", ex);
                        } else {
                            logger.info("Sent message to topic='{}' with offset='{}'", inTopic, result.getRecordMetadata().offset());
                        }
                    });

            if (message.getOperationType() != KafkaPostMessage.OperationType.CREATE) {
                try {
                    return CompletableFuture.supplyAsync(() -> {
                        try {
                            return responseFuture.get(1, TimeUnit.SECONDS);
                        } catch (Exception e) {
                            waitingResponses.remove(responseKey);
                            throw new RuntimeException("Timeout waiting for response", e);
                        }
                    });
                } catch (Exception e) {
                    waitingResponses.remove(responseKey);
                    throw new RuntimeException("Error waiting for response", e);
                }
            }

            return responseFuture;
        } catch (Exception e) {
            waitingResponses.remove(responseKey);
            responseFuture.completeExceptionally(e);
            logger.error("Error in sendAndReceive", e);
            return responseFuture;
        }
    }

    @KafkaListener(topics = "${kafka.topic.out:reaction-out-topic}", groupId = "${spring.kafka.consumer.group-id:publisher-group}")
    public void listen(String message) {
        try {
            logger.info("Received message from topic: {}", message);
            KafkaPostMessage kafkaMessage = objectMapper.readValue(message, KafkaPostMessage.class);

            String responseKey = generateResponseKey(kafkaMessage);
            CompletableFuture<ReactionResponseTo> responseFuture = waitingResponses.remove(responseKey);
            if (responseFuture != null) {
                ReactionResponseTo response = new ReactionResponseTo();
                response.setCountry(kafkaMessage.getCountry());
                response.setIssueId(kafkaMessage.getIssueId());
                response.setId(kafkaMessage.getId());
                response.setContent(kafkaMessage.getContent());
                response.setState(kafkaMessage.getState());

                responseFuture.complete(response);
                logger.info("Response completed for key: {}", responseKey);
            } else {
                logger.warn("No waiting future found for response key: {}", responseKey);
            }
        } catch (Exception e) {
            logger.error("Error processing Kafka message: {}", message, e);
        }
    }

    private String generateResponseKey(KafkaPostMessage message) {
        return message.getOperationType() + ":" +
                ("null") + ":" +
                ("null") + ":" +
                (message.getId() != null ? message.getId() : "null");
    }
}