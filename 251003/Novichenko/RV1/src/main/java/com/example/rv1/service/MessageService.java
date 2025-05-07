package com.example.rv1.service;

import com.example.rv1.dto.MessageDTO;
import com.example.rv1.dto.TweetDTO;
import com.example.rv1.entity.Message;
import com.example.rv1.entity.Tweet;
import com.example.rv1.exception.MyException;
import com.example.rv1.mapper.MessageMapper;
import com.example.rv1.mapper.TweetMapper;
import com.example.rv1.storage.InMemoryStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageMapper messageMapper;
    public MessageDTO createMessage(MessageDTO tweetDTO){
        Message tweet = messageMapper.toMessage(tweetDTO);
        int amount = InMemoryStorage.messages.size();
        tweet.setId(amount);
        InMemoryStorage.messages.add(tweet);
        MessageDTO dto = messageMapper.toMessageDTO(tweet);
        return  dto;
    }

    public MessageDTO deleteMessage(int id) throws Exception {
        int amount = InMemoryStorage.messages.size();
        if (id >= amount){
            throw new MyException("aaaaaaaa");
        }
        Message user = InMemoryStorage.messages.get(id);
        MessageDTO dto = messageMapper.toMessageDTO(user);
        InMemoryStorage.messages.remove(id);
        return  dto;
    }

    public MessageDTO getMessage(int id){
        Message user = InMemoryStorage.messages.get(id);
        MessageDTO dto = messageMapper.toMessageDTO(user);
        return  dto;
    }

    public List<MessageDTO> getMessages(){
        List<Message> users = InMemoryStorage.messages;
        List<MessageDTO> dtos = messageMapper.toMessageDTOList(users);
        return  dtos;
    }

    public MessageDTO updateMessage(MessageDTO userDTO){
        Message user = messageMapper.toMessage(userDTO);
        int id = user.getId();
        InMemoryStorage.messages.set(id,user);
        MessageDTO dto = messageMapper.toMessageDTO(user);
        return  dto;
    }

}
