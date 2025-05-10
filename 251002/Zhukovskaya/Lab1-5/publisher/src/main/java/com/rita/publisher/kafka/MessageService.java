package com.rita.publisher.kafka;

import com.rita.publisher.dto.MessageCreateDTO;
import com.rita.publisher.dto.MessageDTO;
import com.rita.publisher.dto.MessageUpdateDTO;
import com.rita.publisher.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class MessageService {

    @Autowired
    private RedisService redisService;

    public ResponseEntity<MessageDTO> createMessage(MessageCreateDTO messageCreateDTO) {
        MessageDTO result = redisService.createMessageCached(messageCreateDTO);
         return new ResponseEntity<>(result, HttpStatus.CREATED);
         }
    public ResponseEntity<MessageDTO> getMessage(Long id) {
        MessageDTO result = redisService.getMessageCached(id);
        return new ResponseEntity<>(result, HttpStatus.OK);


    }
    public ResponseEntity<List<MessageDTO>> getAllMessages() {
        List<MessageDTO> result = redisService.getAllMessagesCached();
       return new ResponseEntity<>(result, HttpStatus.OK);

    }
    public ResponseEntity<MessageDTO> updateMessage(MessageUpdateDTO messageUpdateDTO)  {
        MessageDTO result = redisService.updateMessageCached(messageUpdateDTO);
       return new ResponseEntity<>(result, HttpStatus.OK);

    }
   public ResponseEntity<Void> deleteMessage(Long id) {
       redisService.deleteMessageCached(id);
       return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }



}
