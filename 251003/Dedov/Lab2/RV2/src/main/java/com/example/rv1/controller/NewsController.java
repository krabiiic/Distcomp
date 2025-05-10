package com.example.rv1.controller;

import com.example.rv1.dto.NewsDTO;
import com.example.rv1.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1.0/news")
@RequiredArgsConstructor
public class NewsController {
    private final NewsService newsService;
    @PostMapping
    public ResponseEntity<NewsDTO> createNews(@Valid @RequestBody NewsDTO newsDTO) {
        NewsDTO dto = newsService.createNews(newsDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<NewsDTO> deleteNews(@PathVariable int id) throws Exception {
        NewsDTO dto = newsService.deleteNews(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/{id}")
    public ResponseEntity<NewsDTO> getNews(@PathVariable int id){
        NewsDTO dto = newsService.getNews(id);
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }
    @GetMapping("")
    public ResponseEntity<List<NewsDTO>> getNews(){
        List<NewsDTO> dto = newsService.getNews();
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @PutMapping
    public ResponseEntity<NewsDTO> updateNews(@Valid @RequestBody NewsDTO newsDTO){
        newsService.updateNews(newsDTO);
        return ResponseEntity.status(HttpStatus.OK).body(newsDTO);
    }
}
