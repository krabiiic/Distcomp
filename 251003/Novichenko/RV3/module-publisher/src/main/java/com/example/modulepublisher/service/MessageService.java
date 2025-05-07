package com.example.modulepublisher.service;

import com.example.modulepublisher.dto.MessageDTO;
import com.example.modulepublisher.entity.Message;
import com.example.modulepublisher.entity.Tweet;
import com.example.modulepublisher.exception.MyException;
import com.example.modulepublisher.mapper.MessageMapper;
import com.example.modulepublisher.repository.MessageRepository;
import com.example.modulepublisher.repository.TweetRepository;
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
