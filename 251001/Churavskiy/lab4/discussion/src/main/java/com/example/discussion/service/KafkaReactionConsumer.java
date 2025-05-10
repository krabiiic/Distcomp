package com.example.discussion.service;

import com.example.discussion.dto.ReactionRequestTo;
import com.example.discussion.dto.ReactionResponseTo;
import com.example.discussion.entity.Reaction;
import com.example.discussion.entity.ReactionKey;
import com.example.discussion.repository.ReactionRepository;
import com.example.discussion.state.ReactionState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaReactionConsumer {

    private final ReactionRepository reactionRepository;
    private final KafkaTemplate<String, ReactionResponseTo> kafkaTemplate;

    private static final Set<String> STOP_WORDS = Set.of("bad", "ugly", "offensive");

    @KafkaListener(topics = "InTopic", groupId = "reaction-group", containerFactory = "reactionRequestListenerFactory")
    public void handleIncomingReaction(ReactionRequestTo request) {
        log.info("Received reaction from Kafka: {}", request);

        ReactionState newState = containsStopWords(request.content())
                ? ReactionState.DECLINE
                : ReactionState.APPROVE;

        Reaction reaction = new Reaction();
        reaction.setId(new ReactionKey(
                request.country() != null ? request.country() : "undefined",
                request.topicId(),
                request.id() != null ? request.id() : generateId()
        ));
        reaction.setContent(request.content());
        reaction.setState(newState);

        reactionRepository.save(reaction);
        log.info("Saved moderated reaction: {}", reaction);

        ReactionResponseTo response = new ReactionResponseTo(
                reaction.getId().getCountry(),
                reaction.getId().getId(),
                reaction.getId().getTopicId(),
                reaction.getContent(),
                reaction.getState()
        );

        kafkaTemplate.send("OutTopic", response);
        log.info("Sent reaction to OutTopic: {}", response);
    }

    private boolean containsStopWords(String content) {
        String lowered = content.toLowerCase();
        return STOP_WORDS.stream().anyMatch(lowered::contains);
    }

    private long generateId() {
        return ThreadLocalRandom.current().nextLong(1, Long.MAX_VALUE);
    }
}