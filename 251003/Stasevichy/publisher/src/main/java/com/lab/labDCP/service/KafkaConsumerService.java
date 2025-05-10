package com.lab.labDCP.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lab.labDCP.dto.NoticeRequestKafkaTo;
import com.lab.labDCP.dto.NoticeResponseKafkaTo;
import com.lab.labDCP.dto.NoticeResponseTo;
import com.lab.labDCP.entity.NoticeKafka;
import com.lab.labDCP.entity.NoticeState;
import com.lab.labDCP.mapper.NoticeKafkaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class KafkaConsumerService {
    private final BlockingQueue<String> messageQueue = new LinkedBlockingQueue<>();
    @Autowired
    private final NoticeKafkaMapper noticeKafkaMapper = new NoticeKafkaMapper() {
        @Override
        public NoticeKafka toEntity(NoticeRequestKafkaTo noticeRequestTo) {
            return new NoticeKafka(noticeRequestTo.getObject(),noticeRequestTo.getNoticeState());
        }
        @Override
        public NoticeResponseKafkaTo toResponseDto(NoticeKafka notices) {
            return new NoticeResponseKafkaTo(notices.getObject(),notices.getNoticeState());
        }
        @Override
        public NoticeKafka toEntity(NoticeResponseKafkaTo noticeResponseTo) {
            return new NoticeKafka(noticeResponseTo.getObject(),noticeResponseTo.getNoticeState());
        }
    };
    @KafkaListener(topics = "InTopic", groupId = "discussion_group")
    public void listen(String message) {
        messageQueue.offer(message);
    }

    public String waitForMessage(long timeoutMillis) {
        try {
            long startTime = System.currentTimeMillis();
            while (System.currentTimeMillis() - startTime < timeoutMillis) {
                String message = messageQueue.poll();
                if (message != null) {
                    return message;
                }
                Thread.sleep(1000);
            }
            return null;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }

    public String waitForMessage() throws InterruptedException {
        try {
            String message = messageQueue.take();
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            NoticeResponseKafkaTo response = mapper.readValue(message, NoticeResponseKafkaTo.class);
            if(response.getNoticeState()==NoticeState.DECLINE){
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new NoticeResponseTo()).toString();
            }
            return mapper.readValue(message, NoticeResponseKafkaTo.class).toString();
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new NoticeResponseTo()).toString();
        }
    }

}
