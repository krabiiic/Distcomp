package com.example.restservice.service;

import com.example.restservice.dto.request.MessageRequestTo;
import com.example.restservice.dto.response.MessageResponseTo;
import com.example.restservice.model.Message;
import com.example.restservice.repository.MessageRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final ModelMapper modelMapper;

    public List<MessageResponseTo> getAll() {
        return messageRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public MessageResponseTo getById(Long id) {
        return messageRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Message not found"));
    }

    public MessageResponseTo create(MessageRequestTo request) {
        Message message = modelMapper.map(request, Message.class);
        Message saved = messageRepository.save(message);
        return toResponse(saved);
    }

    public MessageResponseTo update(MessageRequestTo request) {
        if (request.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Message ID must not be null for update");
        }

        Message existing = messageRepository.findById(request.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Message not found"));

        modelMapper.map(request, existing);
        Message updated = messageRepository.update(existing);
        return toResponse(updated);
    }

    public void delete(Long id) {
        if (messageRepository.findById(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Message not found");
        }
        messageRepository.deleteById(id);
    }

    private MessageResponseTo toResponse(Message message) {
        return modelMapper.map(message, MessageResponseTo.class);
    }
}
