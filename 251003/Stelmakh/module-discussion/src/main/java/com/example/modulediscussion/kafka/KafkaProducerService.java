package com.example.modulediscussion.kafka;

import com.example.modulepublisher.dto.ReactionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {
    private final KafkaTemplate<String, ReactionDTO> kafkaTemplate;

    @Autowired
    public KafkaProducerService(KafkaTemplate<String, ReactionDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendReaction(String topic, ReactionDTO message) {
        kafkaTemplate.send(MessageBuilder.withPayload(message)
                .setHeader(KafkaHeaders.TOPIC, topic)
                .build());
    }
}
