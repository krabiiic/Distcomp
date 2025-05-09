package com.example.publisher.service;

import com.example.publisher.dto.ReactionResponseTo;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class KafkaReactionResponseHandler {

    private final Map<Long, CompletableFuture<ReactionResponseTo>> pending = new ConcurrentHashMap<>();

    public void waitForResponse(Long id, CompletableFuture<ReactionResponseTo> future) {
        pending.put(id, future);
    }

    @KafkaListener(topics = "OutTopic", groupId = "publisher-group", containerFactory = "reactionKafkaListenerFactory")
    public void receive(ReactionResponseTo response) {
        CompletableFuture<ReactionResponseTo> future = pending.remove(response.id());
        if (future != null) {
            future.complete(response);
        }
    }
}
