package com.example.service;

import com.example.dto.KafkaReactionMessage;
import com.example.exception.NotFoundException;
import com.example.model.Reaction;
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
    private final ReactionService reactionService;
    private final ObjectMapper objectMapper;

    @Value("${kafka.topic.out:reaction-out-topic}")
    private String outTopic;

    public DiscussionKafkaService(KafkaTemplate<String, String> kafkaTemplate,
                                  ReactionService reactionService,
                                  ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.reactionService = reactionService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "${kafka.topic.in:reaction-in-topic}", groupId = "${spring.kafka.consumer.group-id:discussion-group}")
    public void listen(String message) {
        try {
            logger.info("Received message: {}", message);
            KafkaReactionMessage kafkaMessage = objectMapper.readValue(message, KafkaReactionMessage.class);

            KafkaReactionMessage response = processMessage(kafkaMessage);

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

    private KafkaReactionMessage processMessage(KafkaReactionMessage message) {
        logger.info("Processing message with operation: {}", message.getOperationType());
        return switch (message.getOperationType()) {
            case CREATE -> createReaction(message);
            case READ -> readReaction(message);
            case UPDATE -> updateReaction(message);
            case DELETE -> deleteReaction(message);
            default -> throw new IllegalArgumentException("Unknown operation type: " + message.getOperationType());
        };
    }

    private KafkaReactionMessage createReaction(KafkaReactionMessage message) {
        Reaction.ReactionKey key = new Reaction.ReactionKey(message.getCountry(), message.getIssueId(), message.getId());
        Reaction reaction = new Reaction(key, message.getContent());
        reaction.setState(Reaction.State.PENDING);

        Reaction saved = reactionService.save(reaction);

        return createResponseMessage(KafkaReactionMessage.OperationType.CREATE, saved);
    }

    private KafkaReactionMessage readReaction(KafkaReactionMessage message) {
        Reaction reaction = reactionService.findById(message.getId())
                .orElseThrow(() -> new NotFoundException("Reaction not found", 40400));
        return createResponseMessage(KafkaReactionMessage.OperationType.READ, reaction);
    }

    private KafkaReactionMessage updateReaction(KafkaReactionMessage message) {
        Reaction.ReactionKey key = new Reaction.ReactionKey(message.getCountry(), message.getIssueId(), message.getId());
        Reaction existingReaction = reactionService.findById(key)
                .orElseThrow(() -> new NotFoundException("Reaction not found", 40401));

        existingReaction.setContent(message.getContent());
        existingReaction.setState(Reaction.State.PENDING);

        Reaction updated = reactionService.save(existingReaction);

        return createResponseMessage(KafkaReactionMessage.OperationType.UPDATE, updated);
    }

    private KafkaReactionMessage deleteReaction(KafkaReactionMessage message) {
        Reaction reaction = reactionService.findById(message.getId())
                .orElseThrow(() -> new NotFoundException("Reaction not found", 40402));

        reactionService.deleteById(reaction.getKey());
        return createResponseMessage(KafkaReactionMessage.OperationType.DELETE, reaction);
    }

    private void sendErrorResponse(String originalMessage, Exception e) {
        try {
            KafkaReactionMessage errorMessage = new KafkaReactionMessage();
            errorMessage.setOperationType(KafkaReactionMessage.OperationType.CREATE); // По умолчанию
            errorMessage.setContent("Error: " + e.getMessage());
            errorMessage.setState(Reaction.State.DECLINE);

            String issueId = "error";
            try {
                KafkaReactionMessage original = objectMapper.readValue(originalMessage, KafkaReactionMessage.class);
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

    private KafkaReactionMessage createResponseMessage(KafkaReactionMessage.OperationType opType, Reaction reaction) {
        KafkaReactionMessage response = new KafkaReactionMessage();
        response.setOperationType(opType);
        response.setCountry(reaction.getKey().getCountry());
        response.setIssueId(reaction.getKey().getIssueId());
        response.setId(reaction.getKey().getId());
        response.setContent(reaction.getContent());
        response.setState(reaction.getState());
        return response;
    }
}