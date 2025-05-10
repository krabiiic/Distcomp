package com.rita.discussion.service;

import com.rita.discussion.dto.MessageCreateDTO;
import com.rita.discussion.dto.MessageDTO;
import com.rita.discussion.dto.MessageUpdateDTO;
import com.rita.discussion.dto.TweetDTO;
import com.rita.discussion.exception.GlobalException;
import com.rita.discussion.mapper.MessageMapper;
import com.rita.discussion.model.Message;
import com.rita.discussion.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;


@Service
public class MessageService {
    @Autowired
    MessageRepository messageRepository;
    @Autowired
    MessageMapper messageMapper;

    @Autowired
    private IdGeneratorService idGeneratorService;
    public MessageDTO getMessage(Long id){
        return messageMapper.toMessageDTO(messageRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Message not found with id: "+id)));
    }

    public List<MessageDTO> getAllMessages(){
        List<MessageDTO> list=new ArrayList<>();
        messageRepository.findAll().forEach(x->list.add(messageMapper.toMessageDTO(x)));
        return list;
    }

    public MessageDTO createMessage(MessageCreateDTO messageDTO){
        if(messageDTO.content().length()<2||messageDTO.content().length()>2048) {
            throw new GlobalException("DB validation", HttpStatus.FORBIDDEN, "DB");

        }
        Message msg = messageMapper.toMessage(messageDTO);
        Long newId = idGeneratorService.getNextId();
        msg.setId(newId);
        return messageMapper.toMessageDTO(messageRepository.save(msg));
    }


    public MessageDTO updateMessage(MessageUpdateDTO messageDTO){

        if(messageDTO.content().length()<2||messageDTO.content().length()>2048) {
            throw new GlobalException("DB validation", HttpStatus.FORBIDDEN, "DB");

        }
       Message message = messageMapper.toMessage(messageDTO);
            Message old = messageRepository.findById(message.getId()).orElseThrow(() -> new EntityNotFoundException("Message not found with id: " + message.getId()));
            return messageMapper.toMessageDTO(messageRepository.save(messageMapper.toMessage(messageDTO)));

    }

    public void deleteMessage(Long id){
        messageRepository.deleteById(id);
    }


    public MessageDTO createMessageExchange(MessageCreateDTO messageDTO){
        if(messageDTO.content().length()<2||messageDTO.content().length()>2048) {
            throw new GlobalException("DB validation", HttpStatus.FORBIDDEN, "DB");

        }
        return messageMapper.toMessageDTO(messageRepository.save(messageMapper.toMessage(messageDTO)));
    }

    public MessageDTO updateMessageExchange(MessageUpdateDTO messageDTO){

        if(messageDTO.content().length()<2||messageDTO.content().length()>2048) {
            throw new GlobalException("DB validation", HttpStatus.FORBIDDEN, "DB");

        }
        Message message=messageMapper.toMessage(messageDTO);
        Message old= messageRepository.findById(message.getId()).orElseThrow(()->new EntityNotFoundException("Message not found with id: "+message.getId()));
        return messageMapper.toMessageDTO(messageRepository.save(messageMapper.toMessage(messageDTO)));
    }



}
