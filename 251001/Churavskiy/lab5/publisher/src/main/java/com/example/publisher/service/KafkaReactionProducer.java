package com.example.publisher.service;

import com.example.publisher.dto.ReactionRequestTo;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
public class KafkaReactionProducer {
    private final KafkaTemplate<String, ReactionRequestTo> kafkaTemplate;

    public void send(ReactionRequestTo reaction) {
        kafkaTemplate.send("InTopic", String.valueOf(reaction.topicId()), reaction);
    }
}
