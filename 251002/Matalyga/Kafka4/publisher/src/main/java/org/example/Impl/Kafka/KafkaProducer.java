package org.example.Impl.Kafka;

import org.example.Impl.DTO.PostRequestTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

    private final KafkaTemplate<String, PostRequestTo> kafkaTemplate;

    @Autowired
    public KafkaProducer(KafkaTemplate<String, PostRequestTo> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(PostRequestTo postRequestTo, String topicName) {
        System.out.println("---SEND FROM 1: " + postRequestTo.toString());
        kafkaTemplate.send(topicName, postRequestTo);
    }
}
