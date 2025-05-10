package com.example.discussion.controller;

import com.example.discussion.dto.NewsRequestTo;
import com.example.discussion.dto.NewsResponseTo;
import com.example.discussion.service.NewsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.List;

@RestController
@RequestMapping("/api/v1.0/news")
public class NewsController {

    private final NewsService newsService;
    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public NewsController(NewsService newsService, RedisTemplate<String, Object> redisTemplate) {
        this.newsService = newsService;
        this.redisTemplate = redisTemplate;
    }

    @GetMapping
    public ResponseEntity<?> getAllNews() {
        try {
            String cacheKey = "allNews";
            List<NewsResponseTo> newsList = (List<NewsResponseTo>) redisTemplate.opsForValue().get(cacheKey);

            if (newsList == null) {
                newsList = newsService.findAll();
                redisTemplate.opsForValue().set(cacheKey, newsList, Duration.ofMinutes(10));
            }
            return new ResponseEntity<>(newsList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error retrieving news list", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getNewsById(@PathVariable Long id) {
        try {
            String cacheKey = "news:" + id;
            NewsResponseTo news = (NewsResponseTo) redisTemplate.opsForValue().get(cacheKey);

            if (news == null) {
                news = newsService.findById(id);
                if (news != null) {
                    redisTemplate.opsForValue().set(cacheKey, news, Duration.ofMinutes(10));
                } else {
                    return new ResponseEntity<>("News not found", HttpStatus.NOT_FOUND);
                }
            }
            return new ResponseEntity<>(news, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error retrieving news item", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<?> createNews(@RequestBody @Valid NewsRequestTo newsRequestTo) {
        try {
            NewsResponseTo createdNews = newsService.save(newsRequestTo);
            redisTemplate.delete("allNews");
            return new ResponseEntity<>(createdNews, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error creating news", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping
    public ResponseEntity<?> updateNews(@RequestBody @Valid NewsRequestTo newsRequestTo) {
        try {
            NewsResponseTo updatedNews = newsService.update(newsRequestTo);
            if (updatedNews != null) {
                redisTemplate.delete("allNews");
                redisTemplate.delete("news:" + updatedNews.getId());
                return new ResponseEntity<>(updatedNews, HttpStatus.OK);
            }
            return new ResponseEntity<>("News not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error updating news", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNews(@PathVariable Long id) {
        try {
            newsService.deleteById(id);
            redisTemplate.delete("allNews");
            redisTemplate.delete("news:" + id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting news", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}