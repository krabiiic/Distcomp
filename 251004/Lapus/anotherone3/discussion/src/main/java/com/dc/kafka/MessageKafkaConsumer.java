package com.dc.kafka;

import com.dc.model.blo.Message;
import com.dc.model.kafka.MessageKafka;
import com.dc.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

@Service
public class MessageKafkaConsumer {

    @Autowired
    private MessageService messageService;

    @KafkaListener(topics = "InTopic", groupId = "discussion-group")
    @SendTo("OutTopic")
    public MessageKafka receive(MessageKafka message) {
        switch (message.getPurpose())
        {
            case "create":
                messageService.save(mapTo(message));
                break;
            case "getAll":
                messageService.getAll();
                break;
            case "update":
                messageService.update(mapTo(message));
                break;
            case "delete":
                messageService.delete(message.getId());
                break;
            case "getId":
                messageService.getById(message.getId());
                break;
        }
        return message;
    }

    private static Message mapTo(MessageKafka m)
    {
        return new Message(m.getId(), m.getNewsId(), m.getContent());
    }
}
