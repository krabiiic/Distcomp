package com.example.modulediscussion.service;

import com.example.modulediscussion.dto.MessageDTO;
import com.example.modulediscussion.entity.Message;
//import com.example.modulediscussion.entity.Tweet;
import com.example.modulediscussion.exception.MyException;
import com.example.modulediscussion.mapper.MessageMapper;
import com.example.modulediscussion.repository.MessageRepository;
//import com.example.modulediscussion.repository.TweetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageMapper messageMapper;
    private final MessageRepository messageRepository;
    private final RestTemplate restTemplate;

    //private final TweetRepository tweetRepository;

    public MessageDTO createMessage(MessageDTO tweetDTO){
        Message tweet = messageMapper.toMessage(tweetDTO);
       //Tweet tweet1 = tweetRepository.findTweetById(tweet.getTweetId()).orElseThrow(() -> new MyException("aaaaaa"));
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
        if(ouser.isEmpty()){
            MessageDTO user = restTemplate.getForObject("http://localhost:24110//api/v1.0/messages/"+ id, MessageDTO.class);
            if (user!=null){
                return  user;
            }
        }
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
