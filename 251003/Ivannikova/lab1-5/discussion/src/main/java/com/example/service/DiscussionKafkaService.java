package com.example.service;

import com.example.dto.KafkaPostMessage;
import com.example.exception.NotFoundException;
import com.example.model.Post;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class DiscussionKafkaService {

    private static final Logger logger = LoggerFactory.getLogger(DiscussionKafkaService.class);

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final PostService reactionService;
    private final ObjectMapper objectMapper;

    @Value("${kafka.topic.out:reaction-out-topic}")
    private String outTopic;

    public DiscussionKafkaService(KafkaTemplate<String, String> kafkaTemplate,
                                  PostService reactionService,
                                  ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.reactionService = reactionService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "${kafka.topic.in:reaction-in-topic}", groupId = "${spring.kafka.consumer.group-id:discussion-group}")
    public void listen(String message) {
        try {
            logger.info("Received message: {}", message);
            KafkaPostMessage kafkaMessage = objectMapper.readValue(message, KafkaPostMessage.class);

            KafkaPostMessage response = processMessage(kafkaMessage);

            String jsonResponse = objectMapper.writeValueAsString(response);

            logger.info("Sending response: {}", jsonResponse);
            kafkaTemplate.send(outTopic, String.valueOf(kafkaMessage.getId()), jsonResponse)
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            logger.error("Error sending response", ex);
                        } else {
                            logger.info("Sent response to topic='{}' with offset='{}'", outTopic, result.getRecordMetadata().offset());
                        }
                    });
        } catch (Exception e) {
            logger.error("Error processing message: {}", message, e);
            sendErrorResponse(message, e);
        }
    }

    private KafkaPostMessage processMessage(KafkaPostMessage message) {
        logger.info("Processing message with operation: {}", message.getOperationType());
        return switch (message.getOperationType()) {
            case CREATE -> createReaction(message);
            case READ -> readReaction(message);
            case UPDATE -> updateReaction(message);
            case DELETE -> deleteReaction(message);
            default -> throw new IllegalArgumentException("Unknown operation type: " + message.getOperationType());
        };
    }

    private KafkaPostMessage createReaction(KafkaPostMessage message) {
        Post.PostKey key = new Post.PostKey(message.getCountry(), message.getIssueId(), message.getId());
        Post reaction = new Post(key, message.getContent());
        reaction.setState(Post.State.PENDING);

        Post saved = reactionService.save(reaction);

        return createResponseMessage(KafkaPostMessage.OperationType.CREATE, saved);
    }

    private KafkaPostMessage readReaction(KafkaPostMessage message) {
        Post reaction = reactionService.findById(message.getId())
                .orElseThrow(() -> new NotFoundException("Reaction not found", 40400));
        return createResponseMessage(KafkaPostMessage.OperationType.READ, reaction);
    }

    private KafkaPostMessage updateReaction(KafkaPostMessage message) {
        Post.PostKey key = new Post.PostKey(message.getCountry(), message.getIssueId(), message.getId());
        Post existingReaction = reactionService.findById(key)
                .orElseThrow(() -> new NotFoundException("Reaction not found", 40401));

        existingReaction.setContent(message.getContent());
        existingReaction.setState(Post.State.PENDING);

        Post updated = reactionService.save(existingReaction);

        return createResponseMessage(KafkaPostMessage.OperationType.UPDATE, updated);
    }

    private KafkaPostMessage deleteReaction(KafkaPostMessage message) {
        Post reaction = reactionService.findById(message.getId())
                .orElseThrow(() -> new NotFoundException("Reaction not found", 40402));

        reactionService.deleteById(reaction.getKey());
        return createResponseMessage(KafkaPostMessage.OperationType.DELETE, reaction);
    }

    private void sendErrorResponse(String originalMessage, Exception e) {
        try {
            KafkaPostMessage errorMessage = new KafkaPostMessage();
            errorMessage.setOperationType(KafkaPostMessage.OperationType.CREATE); // По умолчанию
            errorMessage.setContent("Error: " + e.getMessage());
            errorMessage.setState(Post.State.DECLINE);

            String issueId = "error";
            try {
                KafkaPostMessage original = objectMapper.readValue(originalMessage, KafkaPostMessage.class);
                if (original.getIssueId() != null) {
                    issueId = String.valueOf(original.getIssueId());
                }
            } catch (Exception ignored) {}

            String jsonError = objectMapper.writeValueAsString(errorMessage);
            kafkaTemplate.send(outTopic, issueId, jsonError);
        } catch (Exception ex) {
            logger.error("Failed to send error response", ex);
        }
    }

    private KafkaPostMessage createResponseMessage(KafkaPostMessage.OperationType opType, Post reaction) {
        KafkaPostMessage response = new KafkaPostMessage();
        response.setOperationType(opType);
        response.setCountry(reaction.getKey().getCountry());
        response.setIssueId(reaction.getKey().getIssueId());
        response.setId(reaction.getKey().getId());
        response.setContent(reaction.getContent());
        response.setState(reaction.getState());
        return response;
    }
}