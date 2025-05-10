package com.discussion.rvlab4_discussion.service;


import com.discussion.rvlab4_discussion.dto.MessageRequestTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

public class KafkaConsumerService {

    @Autowired
    private KafkaProducerService kafkaProducerService;
    private MessageService messageService;

    @KafkaListener(topics = "InTopic", groupId = "discussion_group")
    public void listen(String notice) {

        String responseMessage = "Result";
        if(notice.substring(0,3)=="del"){
            messageService.deleteMessage(Long.parseLong(notice.substring(4,notice.length()-4)));
            responseMessage = "Ok";
        }
        if(notice.substring(0,9) == "Get by id"){
            responseMessage = messageService.getAllMessages().toString();
        }
        if(notice.substring(0,7)=="Get all"){
            responseMessage = messageService.getMessageById(Long.parseLong(notice.substring(4,notice.length()-4))).toString();
        }

        if(notice.substring(0,3)=="Put"){
            responseMessage = messageService.updateMessage(Long.parseLong(notice.substring(4,notice.length()-4)), new MessageRequestTo()).toString();
        }
        if(notice.substring(0,3)=="Post"){
            messageService.updateMessage(Long.parseLong(notice.substring(4,notice.length()-4)), new MessageRequestTo()).toString();
            responseMessage = notice;
        }

        kafkaProducerService.sendMessage("OutTopic", responseMessage);
    }


}