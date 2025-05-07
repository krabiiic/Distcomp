package com.lab.labDC.service;

import com.lab.labDC.dto.NoticeResponseKafkaTo;
import com.lab.labDC.entity.NoticeState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;


    public void sendMessage(String topic, String message) {
        NoticeResponseKafkaTo noticeResponseKafkaTo;
        if(message!="{}"){
            noticeResponseKafkaTo = new NoticeResponseKafkaTo(message, NoticeState.APPROVE);
        } else {
            noticeResponseKafkaTo = new NoticeResponseKafkaTo(message, NoticeState.DECLINE);
        }

        kafkaTemplate.send(topic, noticeResponseKafkaTo.toString());
    }
}