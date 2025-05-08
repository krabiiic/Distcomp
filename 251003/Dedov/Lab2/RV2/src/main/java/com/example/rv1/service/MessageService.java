package com.example.rv1.service;

import com.example.rv1.dto.MessageDTO;
import com.example.rv1.entity.Message;
import com.example.rv1.entity.News;
import com.example.rv1.exception.ExceptionBadRequest;
import com.example.rv1.mapper.MessageMapper;
import com.example.rv1.repository.MessageRepository;
import com.example.rv1.repository.NewsRepository;
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
    private final NewsRepository newsRepository;

    public MessageDTO createMessage(MessageDTO tweetDTO){
        Message message = messageMapper.toMessage(tweetDTO);
        News message1 = newsRepository.findNewsById(message.getNewsId()).orElseThrow(() -> new ExceptionBadRequest("400"));
        messageRepository.save(message);
        MessageDTO dto = messageMapper.toMessageDTO(message);
        return  dto;
    }

    public MessageDTO deleteMessage(int id) throws Exception {
        Optional<Message> ouser = messageRepository.findMessageById(id);
        Message user = ouser.orElseThrow(() -> new ExceptionBadRequest("400"));
        MessageDTO dto = messageMapper.toMessageDTO(user);
        messageRepository.delete(user);
        return  dto;
    }

    public MessageDTO getMessage(int id){
        Optional<Message> ouser = messageRepository.findMessageById(id);
        Message message = ouser.orElseThrow(() -> new ExceptionBadRequest("400"));
        MessageDTO dto = messageMapper.toMessageDTO(message);
        return  dto;
    }

    public List<MessageDTO> getMessages(){
        List<Message> messages = InMemoryStorage.messages;
        List<MessageDTO> dtos = messageMapper.toMessageDTOList(messages);
        return  dtos;
    }

    public MessageDTO updateMessage(MessageDTO messageDTO){
        Message message = messageMapper.toMessage(messageDTO);
        messageRepository.save(message);
        MessageDTO dto = messageMapper.toMessageDTO(message);
        return  dto;
    }

}
