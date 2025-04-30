package com.example.restservice.service;

import com.example.restservice.dto.request.MessageRequestTo;
import com.example.restservice.dto.response.MessageResponseTo;
import com.example.restservice.model.Message;
import com.example.restservice.repository.MessageRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public void updateState(Long messageId, Message.MessageState newState) {
        Message message = messageRepository.findById(messageId.toString())
                .orElseThrow(() -> new NoSuchElementException(messageId.toString()));
        message.setState(newState);
        messageRepository.save(message);
    }

    public List<MessageResponseTo> getAll() {
        return messageRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public MessageResponseTo getById(Long id) {
        return messageRepository.findById(String.valueOf(id))
                .map(this::toResponse)
                .orElseThrow(() -> new NoSuchElementException("Message not found"));
    }

    public MessageResponseTo create(MessageRequestTo request) {
        Message message = modelMapper.map(request, Message.class);
        Message saved = messageRepository.save(message);
        return toResponse(saved);
    }

    public MessageResponseTo update(MessageRequestTo request) {
        if (request.getId() == null) {
            throw new NoSuchElementException("Message not found");
        }

        Message existing = messageRepository.findById(String.valueOf(request.getId()))
                .orElseThrow(() -> new NoSuchElementException("Message not found"));

        modelMapper.map(request, existing);
        Message updated = messageRepository.save(existing);
        return toResponse(updated);
    }

    public void delete(Long id) {
        if (messageRepository.findById(String.valueOf(id)).isEmpty()) {
            throw new NoSuchElementException("Message not found");
        }
        messageRepository.deleteById(String.valueOf(id));
    }

    private MessageResponseTo toResponse(Message message) {
        return modelMapper.map(message, MessageResponseTo.class);
    }
}
