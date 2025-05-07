package com.lab.labDC.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lab.labDC.dto.NoticeRequestTo;
import com.lab.labDC.dto.NoticeResponseKafkaTo;
import com.lab.labDC.entity.NoticeState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;

public class KafkaConsumerService {

    @Autowired
    private KafkaProducerService kafkaProducerService;
    private NoticeService noticeService;

    @KafkaListener(topics = "InTopic", groupId = "discussion_group")
    public void listen(String request) {
        String notice;
        try {
            notice = extractValue(request);
        }catch (Exception e){
            notice = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new NoticeRequestTo()).toString();
        }
        String responseMessage = "Result";
        if(notice.substring(0,3)=="del"){
            noticeService.deleteNotice(Long.parseLong(notice.substring(4,notice.length()-4)));
            responseMessage = "Ok";
        }
        if(notice.substring(0,9) == "Get by id"){
            responseMessage = noticeService.getAllNotices().toString();
        }
        if(notice.substring(0,7)=="Get all"){
            responseMessage = noticeService.getNoticeById(Long.parseLong(notice.substring(4,notice.length()-4))).toString();
        }
        if(notice.substring(0,3)=="Put"){
            responseMessage = noticeService.updateNotice(Long.parseLong(notice.substring(4,notice.length()-4)), new NoticeRequestTo()).toString();
        }
        if(notice.substring(0,4)=="Post"){
            noticeService.updateNotice(Long.parseLong(notice.substring(4,notice.length()-4)), new NoticeRequestTo()).toString();
            responseMessage = notice;
        }
        kafkaProducerService.sendMessage("OutTopic", responseMessage);
    }

    public String extractValue(String message) throws InterruptedException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            NoticeResponseKafkaTo response = mapper.readValue(message, NoticeResponseKafkaTo.class);
            return mapper.readValue(message, NoticeResponseKafkaTo.class).toString();
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new NoticeRequestTo()).toString();
        }
    }
    public String extractNotice(String message) throws InterruptedException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            Notice response = mapper.readValue(message, Notice.class);
            return mapper.readValue(message, NoticeResponseKafkaTo.class).toString();
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new NoticeRequestTo()).toString();
        }
    }

}


