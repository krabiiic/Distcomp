package com.example.restservice.controller;

import com.example.restservice.dto.request.ArticleRequestTo;
import com.example.restservice.dto.response.ArticleResponseTo;
import com.example.restservice.service.ArticleService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1.0/articles")
public class ArticleController {
    private final ArticleService articleService;

    @GetMapping
    public ResponseEntity<List<ArticleResponseTo>> getAll() {
        return ResponseEntity.ok(articleService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticleResponseTo> getById(@PathVariable Long id) {
        return ResponseEntity.ok(articleService.getById(id));
    }

    @PostMapping
    public ResponseEntity<ArticleResponseTo> create(@RequestBody ArticleRequestTo request) {
      return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(articleService.create(request));
    }

    @PutMapping
    public ResponseEntity<ArticleResponseTo> update(@RequestBody ArticleRequestTo request) {
        return ResponseEntity.ok(articleService.update(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        articleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}