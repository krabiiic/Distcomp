package com.publisher.rvlab4_publisher.controller;

import com.publisher.rvlab4_publisher.dto.MessageRequestTo;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/v1.0/messages")
public class MessageForwardController {
    private final RestTemplate restTemplate = new RestTemplate();
    private final RedisTemplate<String, Object> redisTemplate;

    public MessageForwardController(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @GetMapping
    public ResponseEntity<Object> getMessage() {
        String cacheKey = "messages:all";
        Object cachedMessages = redisTemplate.opsForValue().get(cacheKey);

        if (cachedMessages != null) {
            return ResponseEntity.ok(cachedMessages);
        }

        String url = "http://localhost:24130/api/v1.0/messages";
        try {
            ResponseEntity<Object> response = restTemplate.getForEntity(url, Object.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                redisTemplate.opsForValue().set(cacheKey, response.getBody());
            }
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching message");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getMessage(@PathVariable String id) {
        String cacheKey = "message:" + id;
        Object cachedMessage = redisTemplate.opsForValue().get(cacheKey);

        if (cachedMessage != null) {
            return ResponseEntity.ok(cachedMessage);
        }

        String url = "http://localhost:24130/api/v1.0/messages/" + id;
        try {
            ResponseEntity<Object> response = restTemplate.getForEntity(url, Object.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                redisTemplate.opsForValue().set(cacheKey, response.getBody());
            }
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping
    public ResponseEntity<Object> createMessage(@RequestBody Object newMessage) {
        String url = "http://localhost:24130/api/v1.0/messages";
        try {
            ResponseEntity<Object> response = restTemplate.postForEntity(url, newMessage, Object.class);
            redisTemplate.delete("messages:all");
            return ResponseEntity.status(HttpStatus.CREATED).body(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating message");
        }
    }

    @PutMapping
    public ResponseEntity<Object> putMessage(@RequestBody MessageRequestTo newMessage) {
        String url = "http://localhost:24130/api/v1.0/messages";
        try {
            restTemplate.put(url, newMessage);
            redisTemplate.delete("message:" + newMessage.getId());
            redisTemplate.delete("messages:all");

            ResponseEntity<Object> response = restTemplate.getForEntity(
                    "http://localhost:24130/api/v1.0/messages/" + newMessage.getId(), Object.class);

            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            System.out.println(e.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteMessage(@PathVariable Long id) {
        String url = "http://localhost:24130/api/v1.0/messages/" + id;
        try {
            restTemplate.delete(url);
            redisTemplate.delete("message:" + id);
            redisTemplate.delete("messages:all");

            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}