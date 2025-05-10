package com.example.restservice.service;

import com.example.restservice.dto.kafka.MessageEvent;
import com.example.restservice.dto.request.MessageRequestTo;
import com.example.restservice.dto.response.MessageResponseTo;
import com.example.restservice.kafka.KafkaMessageProducer;
import com.example.restservice.model.Message;
import com.example.restservice.repository.MessageRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final ModelMapper modelMapper;
    private final KafkaMessageProducer kafkaProducer;
    private final RedisTemplate<String, Object> redisTemplate;
    ObjectMapper objectMapper = new ObjectMapper();

    private static final String CACHE_NAME = "messages";
    private static final long CACHE_TTL_MINUTES = 30;

    @Cacheable(value = CACHE_NAME, key = "'allMessages'")
    public List<MessageResponseTo> getAll() {
        return messageRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public MessageResponseTo getById(Long id) {
        String key = "message:" + id;
        Object cached = redisTemplate.opsForValue().get(key);

        if (cached != null) {
            return objectMapper.convertValue(cached, MessageResponseTo.class);
        }

        return messageRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new NoSuchElementException("Message not found"));
    }

    public MessageResponseTo create(MessageRequestTo request) {
        Message message = modelMapper.map(request, Message.class);
        Message saved = messageRepository.save(message);

        redisTemplate.delete("allMessages");
        redisTemplate.opsForValue().set(
                "message:" + saved.getId(),
                toResponse(saved),
                CACHE_TTL_MINUTES,
                TimeUnit.MINUTES
        );

        kafkaProducer.sendMessage(new MessageEvent(
                saved.getId(),
                saved.getArticle().getId(),
                saved.getContent(),
                saved.getState(),
                LocalDateTime.now(),
                "create"
        ));

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

        redisTemplate.delete("allMessages");
        redisTemplate.delete("message:" + updated.getId());
        redisTemplate.opsForValue().set(
                "message:" + updated.getId(),
                toResponse(updated),
                CACHE_TTL_MINUTES,
                TimeUnit.MINUTES
        );

        kafkaProducer.sendMessage(new MessageEvent(
                updated.getId(),
                updated.getArticle().getId(),
                updated.getContent(),
                updated.getState(),
                LocalDateTime.now(),
                "update"
        ));

        return toResponse(updated);
    }

    public void delete(Long id) {
        if (messageRepository.findById(id).isEmpty()) {
            throw new NoSuchElementException("Message not found");
        }
        messageRepository.deleteById(id);

        redisTemplate.delete("allMessages");
        redisTemplate.delete("message:" + id);

        kafkaProducer.sendMessage(new MessageEvent(
                id, 0L, "",
                Message.MessageState.PENDING,
                LocalDateTime.now(),
                "delete"
        ));
    }


    private MessageResponseTo toResponse(Message message) {
        return modelMapper.map(message, MessageResponseTo.class);
    }
}