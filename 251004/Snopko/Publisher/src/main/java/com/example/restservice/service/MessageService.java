package com.example.restservice.service;

import com.example.restservice.dto.kafka.MessageEvent;
import com.example.restservice.dto.request.MessageRequestTo;
import com.example.restservice.dto.response.MessageResponseTo;
import com.example.restservice.kafka.KafkaMessageProducer;
import com.example.restservice.model.Message;
import com.example.restservice.repository.MessageRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final ModelMapper modelMapper;
    private final KafkaMessageProducer kafkaProducer;

    public List<MessageResponseTo> getAll() {
        return messageRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public MessageResponseTo getById(Long id) {
        return messageRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new NoSuchElementException("Message not found"));
    }

    public MessageResponseTo create(MessageRequestTo request) {
        Message message = modelMapper.map(request, Message.class);
        Message saved = messageRepository.save(message);
        kafkaProducer.sendMessage(new MessageEvent(message.getId(), message.getArticle().getId(), message.getContent(), message.getState(), LocalDateTime.now(), "create"));
        return toResponse(saved);
    }

    public MessageResponseTo update(MessageRequestTo request) {
        if (request.getId() == null) {
            throw new NoSuchElementException("Message not found");
        }

        Message existing = messageRepository.findById(request.getId())
                .orElseThrow(() -> new NoSuchElementException("Message not found"));

        modelMapper.map(request, existing);
        Message updated = messageRepository.save(existing);
        kafkaProducer.sendMessage(new MessageEvent(updated.getId(), updated.getArticle().getId(), updated.getContent(), updated.getState(), LocalDateTime.now(), "update"));

        return toResponse(updated);
    }

    public void delete(Long id) {
        if (messageRepository.findById(id).isEmpty()) {
            throw new NoSuchElementException("Message not found");
        }
        messageRepository.deleteById(id);
        kafkaProducer.sendMessage(new MessageEvent(id, 0L, "", Message.MessageState.PENDING, LocalDateTime.now(), "delete"));
    }

    private MessageResponseTo toResponse(Message message) {
        return modelMapper.map(message, MessageResponseTo.class);
    }
}
