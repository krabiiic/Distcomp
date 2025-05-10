package org.example.kafka;

import org.example.dto.PostResponseTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

    private final KafkaTemplate<String, PostResponseTo> kafkaTemplate;

    @Autowired
    public KafkaProducer(KafkaTemplate<String, PostResponseTo> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(PostResponseTo postResponseTo, String topicName) {
        System.out.println("---SEND FROM 2: " + postResponseTo.toString());
        kafkaTemplate.send(topicName, postResponseTo);
    }

}
