package com.example.discussion.controller;

import com.example.discussion.dto.WriterRequestTo;
import com.example.discussion.dto.WriterResponseTo;
import com.example.discussion.exception.DuplicateLoginException;
import com.example.discussion.service.WriterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.List;

@RestController
@RequestMapping("/api/v1.0/writers")
public class WriterController {

    private final WriterService writerService;
    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public WriterController(WriterService writerService, RedisTemplate<String, Object> redisTemplate) {
        this.writerService = writerService;
        this.redisTemplate = redisTemplate;
    }

    @GetMapping
    public ResponseEntity<?> getAllWriters() {
        try {
            String cacheKey = "allWriters";
            List<WriterResponseTo> writers = (List<WriterResponseTo>) redisTemplate.opsForValue().get(cacheKey);

            if (writers == null) {
                writers = writerService.findAll();
                redisTemplate.opsForValue().set(cacheKey, writers, Duration.ofMinutes(10));
            }
            return new ResponseEntity<>(writers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error fetching writers", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getWriterById(@PathVariable Long id) {
        try {
            String cacheKey = "writer:" + id;
            WriterResponseTo writer = (WriterResponseTo) redisTemplate.opsForValue().get(cacheKey);

            if (writer == null) {
                writer = writerService.findById(id);
                if (writer != null) {
                    redisTemplate.opsForValue().set(cacheKey, writer, Duration.ofMinutes(10));
                } else {
                    return new ResponseEntity<>("Writer not found", HttpStatus.NOT_FOUND);
                }
            }
            return new ResponseEntity<>(writer, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<?> createWriter(@Valid @RequestBody WriterRequestTo writerRequestTo) {
        WriterResponseTo savedWriter = writerService.save(writerRequestTo);
        redisTemplate.delete("allWriters"); // Инвалидация кэша
        return new ResponseEntity<>(savedWriter, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<?> updateWriter(@Valid @RequestBody WriterRequestTo writerRequestTo) {
        try {
            WriterResponseTo updatedWriter = writerService.update(writerRequestTo);
            if (updatedWriter != null) {
                redisTemplate.delete("allWriters");
                redisTemplate.delete("writer:" + updatedWriter.getId());
                return new ResponseEntity<>(updatedWriter, HttpStatus.OK);
            }
            return new ResponseEntity<>("Writer not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error updating writer", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteWriter(@PathVariable Long id) {
        try {
            writerService.deleteById(id);
            redisTemplate.delete("allWriters");
            redisTemplate.delete("writer:" + id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting writer", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}