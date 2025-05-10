package com.example.publisher.service;

import com.example.publisher.dto.ReactionRequestTo;
import com.example.publisher.dto.ReactionResponseTo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class ReactionTransportService {
    private final KafkaReactionProducer producer;
    private final KafkaReactionResponseHandler responseHandler;

    public ReactionResponseTo sendReaction(ReactionRequestTo request) {
        CompletableFuture<ReactionResponseTo> future = new CompletableFuture<>();
        responseHandler.waitForResponse(request.id(), future);

        producer.send(request);

        try {
            return future.get(1, TimeUnit.SECONDS); // timeout = 1s
        } catch (Exception e) {
            throw new RuntimeException("Timeout or error while waiting for response", e);
        }
    }
}
