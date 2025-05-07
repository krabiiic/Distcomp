package com.lab.labDCP.service;

import com.lab.labDCP.dto.NoticeRequestKafkaTo;
import com.lab.labDCP.entity.NoticeState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    public void sendMessage(String topic, String message) {
        String sendMessage = new NoticeRequestKafkaTo(message, NoticeState.PENDING).toString();
        kafkaTemplate.send(topic, message);
    }
}