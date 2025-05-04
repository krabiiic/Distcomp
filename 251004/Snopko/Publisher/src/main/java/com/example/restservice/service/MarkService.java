package com.example.restservice.service;

import com.example.restservice.dto.request.MarkRequestTo;
import com.example.restservice.dto.response.MarkResponseTo;
import com.example.restservice.model.Mark;
import com.example.restservice.repository.MarkRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class MarkService {
    private final MarkRepository markRepository;
    private final ModelMapper modelMapper;
    ObjectMapper objectMapper = new ObjectMapper();
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String CACHE_NAME = "marks";
    private static final long CACHE_TTL_MINUTES = 30;

    @Cacheable(value = CACHE_NAME, key = "'allMarks'")
    public List<MarkResponseTo> getAll() {
        return markRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public MarkResponseTo getById(Long id) {
        String key = "mark:" + id;
        Object cached = redisTemplate.opsForValue().get(key);

        if (cached != null) {
            return objectMapper.convertValue(cached, MarkResponseTo.class);
        }

        return markRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new NoSuchElementException("Mark not found"));
    }

    public MarkResponseTo create(MarkRequestTo request) {
        Mark mark = modelMapper.map(request, Mark.class);
        Mark saved = markRepository.save(mark);

        redisTemplate.delete("allMarks");
        redisTemplate.opsForValue().set(
                "mark:" + saved.getId(),
                toResponse(saved),
                CACHE_TTL_MINUTES,
                TimeUnit.MINUTES
        );
        return toResponse(saved);
    }

    public MarkResponseTo update(MarkRequestTo request) {
        if (request.getId() == null) {
            throw new NoSuchElementException("Mark not found");
        }

        Mark existing = markRepository.findById(request.getId())
                .orElseThrow(() -> new NoSuchElementException("Mark not found"));

        modelMapper.map(request, existing);
        Mark updated = markRepository.save(existing);

        redisTemplate.delete("allMarks");
        redisTemplate.opsForValue().set(
                "mark:" + updated.getId(),
                toResponse(updated),
                CACHE_TTL_MINUTES,
                TimeUnit.MINUTES
        );
        return toResponse(updated);
    }

    public void delete(Long id) {
        if (markRepository.findById(id).isEmpty()) {
            throw new NoSuchElementException("Mark not found");
        }
        redisTemplate.delete("allMarks");
        redisTemplate.delete("mark:" + id);
        markRepository.deleteById(id);
    }

    private MarkResponseTo toResponse(Mark mark) {
        return modelMapper.map(mark, MarkResponseTo.class);
    }
}
