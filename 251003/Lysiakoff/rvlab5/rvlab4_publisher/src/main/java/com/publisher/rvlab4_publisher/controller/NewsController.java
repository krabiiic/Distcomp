package com.publisher.rvlab4_publisher.controller;

import com.publisher.rvlab4_publisher.dto.NewsRequestTo;
import com.publisher.rvlab4_publisher.dto.NewsResponseTo;
import com.publisher.rvlab4_publisher.service.NewsService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1.0/news")
public class NewsController {

    private final NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ExceptionHandler(ResponseStatusException.class)
    public NewsResponseTo createNews(@RequestBody NewsRequestTo newsRequestTo) {
        try {
            if (!validateNewsData(newsRequestTo)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "{}");
            }
            return newsService.createNews(newsRequestTo);
        }
        catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "{}");
        }
    }

    private boolean validateNewsData(NewsRequestTo newsRequestTo) {

        if (newsRequestTo.getTitle().length() < 2 ||
                newsRequestTo.getTitle().length() > 64) {
            return false;
        }
        if (newsRequestTo.getContent().length() < 4 ||
                newsRequestTo.getContent().length() > 2048) {
            return false;
        }

        return true;
    }

    @GetMapping
    public List<NewsResponseTo> getAllNews() {
        return newsService.getAllNews();
    }

    @GetMapping("/{id}")
    public NewsResponseTo getNewsById(@PathVariable Long id) {
        return newsService.getNewsById(id);
    }

    @PutMapping
    public ResponseEntity<NewsResponseTo> updateNews(@RequestBody NewsRequestTo newsRequestTo) {
        System.out.println("Received PUT request with ID: " + newsRequestTo.getId());
        try {
            if (newsRequestTo.getId() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new NewsResponseTo());
            }
            NewsResponseTo updatedNews = newsService.updateNews(newsRequestTo.getId(), newsRequestTo);
            return ResponseEntity.ok(updatedNews);
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new NewsResponseTo());
        }
        catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new NewsResponseTo());
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new NewsResponseTo());
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteNews(@PathVariable Long id) {
        try {
            newsService.deleteNews(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}

