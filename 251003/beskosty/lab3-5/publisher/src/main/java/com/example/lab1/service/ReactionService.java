package com.example.lab1.service;

import com.example.lab1.dto.IssueResponseTo;
import com.example.lab1.dto.KafkaReactionMessage;
import com.example.lab1.dto.ReactionRequestTo;
import com.example.lab1.dto.ReactionResponseTo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ReactionService {

    private static final Logger logger = LoggerFactory.getLogger(ReactionService.class);

    private final PublisherKafkaService kafkaService;
    private final AtomicLong idGenerator = new AtomicLong(1);
    private final IssueService issueService;
    private final RedisCacheService cacheService;

    public ReactionService(PublisherKafkaService kafkaService, IssueService issueService, RedisCacheService cacheService) {
        this.kafkaService = kafkaService;
        this.issueService = issueService;
        this.cacheService = cacheService;
    }

    public ReactionResponseTo createReaction(ReactionRequestTo request) {
        IssueResponseTo issue = issueService.getIssueById(request.getIssueId());
        Long generatedId = request.getId() != null ? request.getId() : idGenerator.getAndIncrement();

        KafkaReactionMessage message = new KafkaReactionMessage();
        message.setOperationType(KafkaReactionMessage.OperationType.CREATE);
        message.setCountry(request.getCountry());
        message.setIssueId(request.getIssueId());
        message.setId(generatedId);
        message.setContent(request.getContent());
        message.setState(KafkaReactionMessage.State.PENDING);

        logger.info("Creating reaction with ID: {}", generatedId);

        CompletableFuture<ReactionResponseTo> future = kafkaService.sendAndReceive(message);

        try {
            ReactionResponseTo response = future.get();
            cacheService.saveReaction(response);
            return response;
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Error creating reaction", e);
            throw new RuntimeException("Error creating reaction", e);
        }
    }

    public ReactionResponseTo updateReaction(ReactionRequestTo request) {
        ReactionResponseTo cachedReaction = cacheService.getReaction(request.getId());

        KafkaReactionMessage message = new KafkaReactionMessage();
        message.setOperationType(KafkaReactionMessage.OperationType.UPDATE);
        message.setCountry(request.getCountry());
        message.setIssueId(request.getIssueId());
        message.setId(request.getId());
        message.setContent(request.getContent());

        try {
            ReactionResponseTo response = kafkaService.sendAndReceive(message).get(1, TimeUnit.SECONDS);
            cacheService.saveReaction(response);
            return response;
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            logger.error("Error updating reaction", e);
            throw new RuntimeException("Error updating reaction", e);
        }
    }

    public ReactionResponseTo getReaction(Long id) {
        ReactionResponseTo cachedReaction = null;
        try {
            cachedReaction = cacheService.getReaction(id);
            if (cachedReaction != null) {
                return cachedReaction;
            }
        } catch (Exception e) {
            logger.warn("Cache access failed: {}", e.getMessage());
        }

        KafkaReactionMessage message = new KafkaReactionMessage();
        message.setOperationType(KafkaReactionMessage.OperationType.READ);
        message.setId(id);

        try {
            ReactionResponseTo response = kafkaService.sendAndReceive(message).get(1, TimeUnit.SECONDS);
            try {
                if (response != null) {
                    cacheService.saveReaction(response);
                }
            } catch (Exception e) {
                logger.error("Failed to cache result: {}", e.getMessage());
            }
            return response;
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException("Error getting reaction", e);
        }
    }


    public List<ReactionResponseTo> getAllReactions() {
        return new ArrayList<>();
    }

    public void deleteReaction(Long id) {
        KafkaReactionMessage message = new KafkaReactionMessage();
        message.setOperationType(KafkaReactionMessage.OperationType.DELETE);
        message.setId(id);

        try {
            kafkaService.sendAndReceive(message).get(1, TimeUnit.SECONDS);
            cacheService.deleteReaction(id);
        }
        catch (InterruptedException | ExecutionException | TimeoutException e) {
            logger.error("Error deleting reaction", e);
            throw new RuntimeException("Error deleting reaction", e);
        }
    }
}