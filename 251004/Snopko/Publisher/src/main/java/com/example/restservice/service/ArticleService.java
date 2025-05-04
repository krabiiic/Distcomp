package com.example.restservice.service;

import com.example.restservice.dto.request.ArticleRequestTo;
import com.example.restservice.dto.response.ArticleResponseTo;
import com.example.restservice.dto.response.MarkResponseTo;
import com.example.restservice.model.Article;
import com.example.restservice.model.Mark;
import com.example.restservice.repository.ArticleRepository;
import com.example.restservice.repository.MarkRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final MarkRepository markRepository;
    private final ModelMapper modelMapper;
    ObjectMapper objectMapper = new ObjectMapper();
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String CACHE_NAME = "articles";
    private static final long CACHE_TTL_MINUTES = 30;


    @Cacheable(value = CACHE_NAME, key = "'allArticles'")
    public List<ArticleResponseTo> getAll() {
        return articleRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ArticleResponseTo getById(Long id) {
        String key = "article:" + id;
        Object cached = redisTemplate.opsForValue().get(key);

        if (cached != null) {
            return objectMapper.convertValue(cached, ArticleResponseTo.class);
        }

        return articleRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new NoSuchElementException("Article not found"));
    }

    public ArticleResponseTo create(ArticleRequestTo request) {
        Article article = modelMapper.map(request, Article.class);
        if (request.getMarks() != null && !request.getMarks().isEmpty()) {
            List<Mark> marks = request.getMarks().stream()
                    .map(markName -> markRepository.findByName(markName)
                            .orElseGet(() -> {
                                Mark newMark = new Mark();
                                newMark.setName(markName);
                                redisTemplate.delete("allMarks");
                                redisTemplate.opsForValue().set(
                                        "mark:" + newMark.getId(),
                                        toResponseMark(newMark),
                                        CACHE_TTL_MINUTES,
                                        TimeUnit.MINUTES
                                );
                                return markRepository.save(newMark);
                            }))
                    .collect(Collectors.toList());
            article.setMarks(marks);
        }
        Article saved = articleRepository.save(article);

        redisTemplate.delete("allArticles");
        redisTemplate.opsForValue().set(
                "article:" + saved.getId(),
                toResponse(saved),
                CACHE_TTL_MINUTES,
                TimeUnit.MINUTES
        );
        return toResponse(saved);
    }

    public ArticleResponseTo update(ArticleRequestTo request) {
        if (request.getId() == null) {
            throw new NoSuchElementException("Article not found");
        }

        Article existing = articleRepository.findById(request.getId())
                .orElseThrow(() -> new NoSuchElementException("Article not found"));

        modelMapper.map(request, existing);

        if (request.getMarks() != null && !request.getMarks().isEmpty()) {
            List<Mark> marks = request.getMarks().stream()
                    .map(markName -> markRepository.findByName(markName)
                            .orElseGet(() -> {
                                Mark newMark = new Mark();
                                newMark.setName(markName);
                                redisTemplate.delete("allMarks");
                                redisTemplate.opsForValue().set(
                                        "mark:" + newMark.getId(),
                                        toResponseMark(newMark),
                                        CACHE_TTL_MINUTES,
                                        TimeUnit.MINUTES
                                );
                                return markRepository.save(newMark);
                            }))
                    .collect(Collectors.toList());
            existing.setMarks(marks);
        } else {
            existing.setMarks(null);
        }

        Article updated = articleRepository.save(existing);
        redisTemplate.delete("allArticles");
        redisTemplate.opsForValue().set(
                "article:" + updated.getId(),
                toResponse(updated),
                CACHE_TTL_MINUTES,
                TimeUnit.MINUTES
        );
        return toResponse(updated);
    }

    public void delete(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Article not found"));

        List<Mark> associatedMarks = new ArrayList<>(article.getMarks());
        article.setMarks(null);
        articleRepository.save(article);
        redisTemplate.delete("article:" + id);
        redisTemplate.delete("allArticles");
        articleRepository.deleteById(id);
        for (Mark mark : associatedMarks) {
            if (mark.getArticles().size() <= 1) {
                markRepository.delete(mark);
            }
        }
    }
    private ArticleResponseTo toResponse(Article article) {
        return modelMapper.map(article, ArticleResponseTo.class);
    }
    private MarkResponseTo toResponseMark(Mark mark) {
        return modelMapper.map(mark, MarkResponseTo.class);
    }
}
