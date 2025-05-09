package com.rita.publisher.redis;

import com.rita.publisher.dto.MessageCreateDTO;
import com.rita.publisher.dto.MessageDTO;
import com.rita.publisher.dto.MessageUpdateDTO;
import com.rita.publisher.exception.GlobalException;
import com.rita.publisher.kafka.IdGenerator;
import com.rita.publisher.kafka.KafkaClient;
import com.rita.publisher.kafka.MessageEvent;
import com.rita.publisher.kafka.MessageMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@CacheConfig(cacheNames = "messageCache")
public class RedisService {

    private String requestTopic="InTopic";
    private String requestReplyTopic="OutTopic";

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private KafkaClient kafkaClient;

    @CacheEvict(cacheNames = "messages", allEntries = true)
    public MessageDTO createMessageCached(MessageCreateDTO messageCreateDTO) {

        Long newId = idGenerator.getNextId();
        MessageEvent messageEvent= new MessageEvent(UUID.randomUUID(), MessageEvent.Operation.CREATE,null, new MessageUpdateDTO(newId,messageCreateDTO.tweetId(),messageCreateDTO.content()),null,false);
        // message.setState(State.PENDING);
        MessageEvent result = kafkaClient.sync(messageEvent);
        if(result!=null) {
            if (result.response() != null && result.isException()==false)
                return result.response().getFirst();
            else throw new GlobalException("DB validation", HttpStatus.FORBIDDEN, "DB");
        }
        else throw new GlobalException("DB validation", HttpStatus.FORBIDDEN, "DB");
    }
    @Cacheable(cacheNames = "message", key = "#id", unless = "#result == null")
    public MessageDTO getMessageCached(Long id) {
        MessageEvent messageEvent= new MessageEvent(UUID.randomUUID(), MessageEvent.Operation.FIND_BY_ID,id, null,null,false);
        MessageEvent result = kafkaClient.sync(messageEvent);
        if(result!=null) {
            if (result.response() != null && result.isException()==false)
                return result.response().getFirst();
            else throw new EntityNotFoundException("Message not found");
        }
        else throw new  EntityNotFoundException("Message not found");

    }
    @Cacheable(cacheNames = "messages")
    public List<MessageDTO> getAllMessagesCached() {
        MessageEvent messageEvent= new MessageEvent(UUID.randomUUID(), MessageEvent.Operation.FIND_ALL,null, null,null,false);
        MessageEvent result = kafkaClient.sync(messageEvent);
        if(result!=null) {
            if (result.response() != null && result.isException()==false)
                return result.response();
            else throw new EntityNotFoundException("Message not found");
        }
        else throw new  EntityNotFoundException("Message not found");
    }
    @Caching(evict = { @CacheEvict(cacheNames = "message", key = "#messageUpdateDTO.id"),
            @CacheEvict(cacheNames = "messages", allEntries = true) })
    public MessageDTO updateMessageCached(MessageUpdateDTO messageUpdateDTO)  {
        MessageEvent messageEvent= new MessageEvent(UUID.randomUUID(), MessageEvent.Operation.UPDATE,null, messageUpdateDTO,null,false);
        MessageEvent result = kafkaClient.sync(messageEvent);
        if(result!=null) {
            if (result.response() != null && result.isException()==false)
                return result.response().getFirst();
            else throw new EntityNotFoundException("Message not found");
        }
        else throw new  EntityNotFoundException("Message not found");
    }
    @Caching(evict = { @CacheEvict(cacheNames = "message", key = "#id"),
            @CacheEvict(cacheNames = "messages", allEntries = true) })
    public Void deleteMessageCached(Long id) {
        MessageEvent messageEvent= new MessageEvent(UUID.randomUUID(), MessageEvent.Operation.DELETE_BY_ID,id, null,null,false);
        MessageEvent result = kafkaClient.sync(messageEvent);
        if(result!=null) {
            if (result.isException()==false)
                return null;
            else throw new EntityNotFoundException("Message not found");
        }
        else throw new  EntityNotFoundException("Message not found");
    }
}
