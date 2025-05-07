package com.dc.kafka;

import com.dc.model.kafka.MessageKafka;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageKafkaService {

    @Autowired
    private  KafkaTemplate<Long, MessageKafka> kafkaTemplate;

    public void sendNews(MessageKafka message) {
        kafkaTemplate.send("InTopic", message.getNewsId(), message);
    }

}
