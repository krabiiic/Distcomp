package com.example.publisher.service;

import com.example.publisher.dto.ReactionRequestTo;
import com.example.publisher.dto.ReactionResponseTo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class ReactionService {

    private final WebClient reactionWebClient;

    public Mono<ReactionResponseTo> createReaction(ReactionRequestTo requestTo) {
        return reactionWebClient.post()
                .uri("")
                .bodyValue(requestTo)
                .retrieve()
                .bodyToMono(ReactionResponseTo.class);
    }

    public Mono<ReactionResponseTo> getReactionById(Long id) {
        return reactionWebClient.get()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(ReactionResponseTo.class);
    }

    public Mono<Void> deleteReaction(Long id) {
        return reactionWebClient.delete()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(Void.class);
    }

    public Flux<ReactionResponseTo> getAllReactions() {
        return reactionWebClient.get()
                .uri("")
                .retrieve()
                .bodyToFlux(ReactionResponseTo.class);
    }

    public ReactionResponseTo updateReaction(ReactionRequestTo reactionRequestTo) {
        return reactionWebClient.put()
                .uri("")
                .bodyValue(reactionRequestTo)
                .retrieve()
                .bodyToMono(ReactionResponseTo.class)
                .block();
    }
}
