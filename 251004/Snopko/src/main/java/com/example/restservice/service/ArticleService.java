package com.example.restservice.service;

import com.example.restservice.dto.request.ArticleRequestTo;
import com.example.restservice.dto.response.ArticleResponseTo;
import com.example.restservice.model.Article;
import com.example.restservice.repository.ArticleRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final ModelMapper modelMapper;

    public List<ArticleResponseTo> getAll() {
        return articleRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ArticleResponseTo getById(Long id) {
        return articleRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Article not found"));
    }

    public ArticleResponseTo create(ArticleRequestTo request) {
        Article article = modelMapper.map(request, Article.class);
        Article saved = articleRepository.save(article);
        return toResponse(saved);
    }

    public ArticleResponseTo update(ArticleRequestTo request) {
        if (request.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Article ID must not be null for update");
        }

        Article existing = articleRepository.findById(request.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Article not found"));

        modelMapper.map(request, existing);
        Article updated = articleRepository.update(existing);
        return toResponse(updated);
    }

    public void delete(Long id) {
        if (articleRepository.findById(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Article not found");
        }
        articleRepository.deleteById(id);
    }

    private ArticleResponseTo toResponse(Article article) {
        return modelMapper.map(article, ArticleResponseTo.class);
    }
}
