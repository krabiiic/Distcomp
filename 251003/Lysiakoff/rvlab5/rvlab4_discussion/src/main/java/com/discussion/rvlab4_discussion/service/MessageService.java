package com.discussion.rvlab4_discussion.service;

import com.discussion.rvlab4_discussion.dto.MessageRequestTo;
import com.discussion.rvlab4_discussion.dto.MessageResponseTo;
import com.discussion.rvlab4_discussion.entity.Message;
import com.discussion.rvlab4_discussion.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;

    public List<MessageResponseTo> getAllMessages() {
        return messageRepository.findAll().stream()
                .map(message -> new MessageResponseTo(message.getId(), message.getContent(), message.getNewsId()))
                .collect(Collectors.toList());
    }

    public MessageResponseTo getMessageById(Long id) {
        return messageRepository.findById(id)
                .map(message -> new MessageResponseTo(message.getId(), message.getContent(), message.getNewsId()))
                .orElseThrow(() -> new NoSuchElementException("Message not found"));
    }

    public MessageResponseTo createMessage(MessageRequestTo request) {
        Message message = new Message();
        message.setContent(request.getContent());
        message.setNewsId(request.getNewsId());

        Message savedMessage = messageRepository.save(message);

        return new MessageResponseTo(savedMessage.getId(), savedMessage.getContent(), savedMessage.getNewsId());
    }

    public MessageResponseTo updateMessage(Long id, MessageRequestTo request) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Message not found"));
        message.setContent(request.getContent());
        message.setNewsId(request.getNewsId());
        messageRepository.save(message);
        return new MessageResponseTo(id, message.getContent(), message.getNewsId());
    }

    public void deleteMessage(Long id) {
        Message message = messageRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Message not found"));
        messageRepository.deleteById(id);
    }
}
