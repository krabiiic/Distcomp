package com.example.rv1.service;

import com.example.rv1.dto.MessageDTO;
import com.example.rv1.dto.TweetDTO;
import com.example.rv1.entity.Message;
import com.example.rv1.entity.Tweet;
import com.example.rv1.exception.MyException;
import com.example.rv1.mapper.MessageMapper;
import com.example.rv1.mapper.TweetMapper;
import com.example.rv1.repository.MessageRepository;
import com.example.rv1.repository.TweetRepository;
import com.example.rv1.storage.InMemoryStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageMapper messageMapper;
    private final MessageRepository messageRepository;
    private final TweetRepository tweetRepository;

    public MessageDTO createMessage(MessageDTO tweetDTO){
        Message tweet = messageMapper.toMessage(tweetDTO);
        Tweet tweet1 = tweetRepository.findTweetById(tweet.getTweetId()).orElseThrow(() -> new MyException("aaaaaa"));
        messageRepository.save(tweet);
        MessageDTO dto = messageMapper.toMessageDTO(tweet);
        return  dto;
    }

    public MessageDTO deleteMessage(int id) throws Exception {
        Optional<Message> ouser = messageRepository.findUserById(id);
        Message user = ouser.orElseThrow(() -> new MyException("aaaaaa"));
        MessageDTO dto = messageMapper.toMessageDTO(user);
        messageRepository.delete(user);
        return  dto;
    }

    public MessageDTO getMessage(int id){
        Optional<Message> ouser = messageRepository.findUserById(id);
        Message user = ouser.orElseThrow(() -> new MyException("aaaaaa"));
        MessageDTO dto = messageMapper.toMessageDTO(user);
        return  dto;
    }

    public List<MessageDTO> getMessages(){
        List<Message> users = messageRepository.findAll();
        List<MessageDTO> dtos = messageMapper.toMessageDTOList(users);
        return  dtos;
    }

    public MessageDTO updateMessage(MessageDTO userDTO){
        Message tweet = messageMapper.toMessage(userDTO);
        messageRepository.save(tweet);
        MessageDTO dto = messageMapper.toMessageDTO(tweet);
        return  dto;
    }

}
