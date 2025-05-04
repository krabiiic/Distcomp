package com.example.restservice.service;

import com.example.restservice.dto.request.UserRequestTo;
import com.example.restservice.dto.response.UserResponseTo;
import com.example.restservice.model.User;
import com.example.restservice.repository.UserRepository;

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
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    private final RedisTemplate<String, Object> redisTemplate;
    ObjectMapper objectMapper = new ObjectMapper();

    private static final String CACHE_NAME = "users";
    private static final long CACHE_TTL_MINUTES = 30;


    @Cacheable(value = CACHE_NAME, key = "'allUsers'")
    public List<UserResponseTo> getAll() {
        return userRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public UserResponseTo getById(Long id) {
        String key = "user:" + id;
        Object cached = redisTemplate.opsForValue().get(key);

        if (cached != null) {
            return objectMapper.convertValue(cached, UserResponseTo.class);
        }

        return userRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
    }

    public UserResponseTo create(UserRequestTo request) {
        User user = modelMapper.map(request, User.class);
        User saved = userRepository.save(user);

        redisTemplate.delete("allUsers");
        redisTemplate.opsForValue().set(
                "user:" + saved.getId(),
                toResponse(saved),
                CACHE_TTL_MINUTES,
                TimeUnit.MINUTES
        );
        return toResponse(saved);
    }

    public UserResponseTo update(UserRequestTo request) {
        if (request.getId() == null) {
            throw new NoSuchElementException("User not found");
        }
        User existing = userRepository.findById(request.getId())
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        modelMapper.map(request, existing);

        User updated = userRepository.save(existing);

        redisTemplate.delete("allUsers");
        redisTemplate.opsForValue().set(
                "user:" + updated.getId(),
                toResponse(updated),
                CACHE_TTL_MINUTES,
                TimeUnit.MINUTES
        );
        return toResponse(updated);
    }

    public void delete(Long id) {
        if (userRepository.findById(id).isEmpty()) {
            throw new NoSuchElementException("User not found");
        }

        redisTemplate.delete("allUsers");
        redisTemplate.delete("user:" + id);

        userRepository.deleteById(id);
    }

    private UserResponseTo toResponse(User user) {
        return modelMapper.map(user, UserResponseTo.class);
    }
}
