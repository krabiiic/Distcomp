package com.example.publisher.service;

import com.example.publisher.dto.ReactionRequestTo;
import com.example.publisher.dto.ReactionResponseTo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class ReactionService {

    private final WebClient reactionWebClient;
    private final RedisTemplate<String, ReactionResponseTo> redisTemplate;

    private static final String PREFIX = "reaction::";

    public ReactionResponseTo createReaction(ReactionRequestTo requestTo) {
        ReactionResponseTo response = reactionWebClient.post()
                .uri("")
                .bodyValue(requestTo)
                .retrieve()
                .bodyToMono(ReactionResponseTo.class)
                .block();

        if (response != null) {
            redisTemplate.opsForValue().set(PREFIX + response.id(), response);
        }

        return response;
    }

    public ReactionResponseTo getReactionById(Long id) {
        String key = PREFIX + id;

        ReactionResponseTo cached = redisTemplate.opsForValue().get(key);
        if (cached != null) {
            return cached;
        }

        ReactionResponseTo response = reactionWebClient.get()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(ReactionResponseTo.class)
                .block();

        if (response != null) {
            redisTemplate.opsForValue().set(key, response);
        }

        return response;
    }

    public void deleteReaction(Long id) {
        reactionWebClient.delete()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(Void.class)
                .block();

        redisTemplate.delete(PREFIX + id);
    }

    public Iterable<ReactionResponseTo> getAllReactions() {
        return reactionWebClient.get()
                .uri("")
                .retrieve()
                .bodyToFlux(ReactionResponseTo.class)
                .collectList()
                .block(); // можно сохранить в Redis, если есть смысл кэшировать список
    }

    public ReactionResponseTo updateReaction(ReactionRequestTo reactionRequestTo) {
        ReactionResponseTo response = reactionWebClient.put()
                .uri("")
                .bodyValue(reactionRequestTo)
                .retrieve()
                .bodyToMono(ReactionResponseTo.class)
                .block();

        if (response != null) {
            redisTemplate.opsForValue().set(PREFIX + response.id(), response);
        }

        return response;
    }
}
