package com.publisher.rvlab4_publisher.service;

import org.springframework.kafka.annotation.KafkaListener;

public class KafkaConsumerService {
    @KafkaListener(topics = "InTopic", groupId = "discussion_group")
    public void listen(String notice) {
        String responseMessage = "Processed notice with ID: " + notice;
    }
}