package org.ikrotsyuk.bsuir.modulepublisher.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ikrotsyuk.bsuir.modulepublisher.dto.request.ArticleRequestDTO;
import org.ikrotsyuk.bsuir.modulepublisher.dto.response.ArticleResponseDTO;
import org.ikrotsyuk.bsuir.modulepublisher.service.ArticleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/api/v1.0/articles")
public class ArticleController {
    private final ArticleService articleService;

    @GetMapping
    public ResponseEntity<List<ArticleResponseDTO>> getArticles(){
        return new ResponseEntity<>(articleService.getArticles(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticleResponseDTO> getArticle(@PathVariable Long id){
        return new ResponseEntity<>(articleService.getArticleById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ArticleResponseDTO> addArticle(@Valid @RequestBody ArticleRequestDTO articleRequestDTO){
        return new ResponseEntity<>(articleService.addArticle(articleRequestDTO), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ArticleResponseDTO> deleteArticle(@PathVariable Long id){
        return new ResponseEntity<>(articleService.deleteArticle(id), HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArticleResponseDTO> updateArticle(@PathVariable Long id, @Valid @RequestBody ArticleRequestDTO articleRequestDTO){
        return new ResponseEntity<>(articleService.updateArticle(id, articleRequestDTO), HttpStatus.OK);
    }
}
