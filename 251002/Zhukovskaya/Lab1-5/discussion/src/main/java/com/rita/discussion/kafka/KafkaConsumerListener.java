package com.rita.discussion.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rita.discussion.dto.MessageCreateDTO;
import com.rita.discussion.dto.MessageDTO;
import com.rita.discussion.dto.MessageUpdateDTO;
import com.rita.discussion.exception.GlobalException;
import com.rita.discussion.exception.GlobalExceptionHandler;
import com.rita.discussion.mapper.MessageMapper;
import com.rita.discussion.model.Message;
import com.rita.discussion.repository.MessageRepository;
import com.rita.discussion.service.MessageService;
import jakarta.persistence.EntityNotFoundException;
import lombok.SneakyThrows;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class KafkaConsumerListener {
    @Autowired
    private MessageService messageService;
    @Autowired
    private ObjectMapper json;

    @SneakyThrows
    @KafkaListener(topics = "InTopic", groupId = "discussion-group")
    @SendTo("OutTopic")
    public String listenForMessage( ConsumerRecord<String, String> record) {
        MessageEvent message=json.readValue(record.value(), MessageEvent.class);

        switch (message.operation()) {
            case CREATE:
                return json.writeValueAsString(sendOnCreate(message));
            case FIND_BY_ID:
                return json.writeValueAsString(sendOnGet(message));
            case FIND_ALL:
                return json.writeValueAsString(sendOnGetAll(message));
            case UPDATE:
                return json.writeValueAsString(sendOnUpdate(message));
            case DELETE_BY_ID:
                return json.writeValueAsString(sendOnDelete(message));
        }
        return json.writeValueAsString(new MessageEvent(message.mapKey(),null,null,null,null, true));

    }
    private MessageEvent sendOnCreate(MessageEvent message){

        MessageUpdateDTO dto = message.message();
        if(dto==null)
            return new MessageEvent(message.mapKey(),null,null,null,null, true);
        try{
            MessageDTO messageDTO = messageService.createMessage(new MessageCreateDTO(dto.tweetId(),dto.content()));
            return new MessageEvent(message.mapKey(),null,null,null, Arrays.asList(messageDTO), false);

        }catch( GlobalException e){
            return new MessageEvent(message.mapKey(),null,null,null,null, true);
        }
    }

    private MessageEvent sendOnUpdate(MessageEvent message){
        MessageUpdateDTO dto = message.message();
        if(dto==null)
            return new MessageEvent(message.mapKey(),null,null,null,null, true);
        try{
            MessageDTO messageDTO = messageService.updateMessage(dto);
            return new MessageEvent(message.mapKey(),null,null,null, Arrays.asList(messageDTO), false);

        }catch( GlobalException e){
            return new MessageEvent(message.mapKey(),null,null,null,null, true);
        }
    }

    private MessageEvent sendOnGet(MessageEvent message){

        Long id=message.byId();
        if(id==null)
            return new MessageEvent(message.mapKey(),null,null,null,null, true);
        try{
            MessageDTO messageDTO = messageService.getMessage(id);
            return new MessageEvent(message.mapKey(),null,null,null, Arrays.asList(messageDTO), false);

        }catch( EntityNotFoundException e){
            return new MessageEvent(message.mapKey(),null,null,null,null, true);
        }
    }
    private MessageEvent sendOnGetAll(MessageEvent message){
        try{
            List<MessageDTO> messageDTO = messageService.getAllMessages();
            return new MessageEvent(message.mapKey(),null,null,null, messageDTO, false);

        }catch(GlobalException e){
            return new MessageEvent(message.mapKey(),null,null,null,null, true);
        }
    }

    private MessageEvent sendOnDelete(MessageEvent message){
        Long id=message.byId();
        if(id==null)
            return new MessageEvent(message.mapKey(),null,null,null,null, true);
        try{
            messageService.deleteMessage(id);
            return new MessageEvent(message.mapKey(),null,null,null, null, false);

        }catch(GlobalException e){
            return new MessageEvent(message.mapKey(),null,null,null,null, true);
        }
    }
}
