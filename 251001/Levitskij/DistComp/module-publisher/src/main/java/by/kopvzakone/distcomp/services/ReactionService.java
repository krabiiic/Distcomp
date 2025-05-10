package by.kopvzakone.distcomp.services;

import by.kopvzakone.distcomp.dto.*;
import by.kopvzakone.distcomp.kafka.KafkaClient;
import by.kopvzakone.distcomp.kafka.MessageData;
import by.kopvzakone.distcomp.repositories.EditorRepository;
import by.kopvzakone.distcomp.repositories.TweetRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

@Service
@AllArgsConstructor
public class ReactionService {
    @Qualifier("reactionMapper")
    public final ReactionMapper mapper;
    private final KafkaClient kafkaClient;
    private final WebClient webClient;
    private final TweetRepository tweetRepository;
    public List<ReactionResponseTo> getAll() {
        try {
            return kafkaClient.send(new MessageData(MessageData.Operation.GET_ALL)).responseTOs();
        }catch (TimeoutException e) {
            return webClient.get()
                    .uri("/api/v1.0/reactions")
                    .retrieve()
                    .bodyToFlux(ReactionResponseTo.class).collectList().block();
        }
    }
    @Cacheable(value = "reactions", key = "#id")
    public ReactionResponseTo getById(Long id) {
        try {
            return kafkaClient.send(new MessageData(MessageData.Operation.GET_BY_ID, id)).responseTOs().get(0);
        }catch (TimeoutException e) {
            return webClient.get()
                    .uri("/api/v1.0/reactions/{id}", id)
                    .retrieve()
                    .bodyToMono(ReactionResponseTo.class).block();
        }
    }
    @CachePut(value = "reactions", key = "#req.id")
    public ReactionResponseTo create(ReactionRequestTo req) {
        long tweetId = req.getTweetId();
        if(tweetRepository.get(tweetId).isEmpty())
            throw new DataIntegrityViolationException("Tweet not found for id " + tweetId);
        UUID uuid = UUID.randomUUID();
        long id =  Math.abs(uuid.getMostSignificantBits());
        req.setId(id);
        try {
            return kafkaClient.send(new MessageData(MessageData.Operation.CREATE, req)).responseTOs().get(0);
        }catch (TimeoutException e) {
            return webClient.post()
                    .uri("/api/v1.0/reactions")
                    .bodyValue(req)
                    .retrieve()
                    .bodyToMono(ReactionResponseTo.class).block();
        }
    }
    @CachePut(value = "reactions", key = "#req.id")
    public ReactionResponseTo update(ReactionRequestTo req) {
        long tweetId = req.getTweetId();
        if(tweetRepository.get(tweetId).isEmpty())
            throw new DataIntegrityViolationException("Tweet not found for id " + tweetId);
        try {
            return kafkaClient.send(new MessageData(MessageData.Operation.UPDATE, req)).responseTOs().get(0);
        }catch (TimeoutException e) {
        return webClient.put()
                .uri("/api/v1.0/reactions")
                .bodyValue(req)
                .retrieve()
                .bodyToMono(ReactionResponseTo.class).block();
        }
    }
    @CacheEvict(value = "reactions", key = "#id")
    public void delete(Long id) {
        try {
            kafkaClient.send(new MessageData(MessageData.Operation.DELETE_BY_ID, id));
        }catch (TimeoutException e) {
            webClient.delete()
                    .uri("/api/v1.0/reactions/{id}", id)
                    .retrieve()
                    .bodyToMono(Void.class).block();
        }
    }
}

