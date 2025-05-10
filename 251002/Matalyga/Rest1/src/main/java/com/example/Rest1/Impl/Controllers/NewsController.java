package com.example.Rest1.Impl.Controllers;

import com.example.Rest1.Impl.DTO.NewsResponseTo;
import com.example.Rest1.Impl.DTO.NewsRequestTo;
import com.example.Rest1.Impl.Services.NewsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1.0")
public class NewsController {
    @Autowired
    private NewsService newsService;

    @GetMapping("/news")
    public ResponseEntity<List<NewsResponseTo>> getAllNews() {
        List<NewsResponseTo> newsResponseToList = newsService.getAll();
        return new ResponseEntity<>(newsResponseToList, HttpStatus.OK);
    }

    @GetMapping("/news/{id}")
    public ResponseEntity<NewsResponseTo> getNews(@PathVariable long id) {
        NewsResponseTo newsResponseTo = newsService.get(id);
        return new ResponseEntity<>(newsResponseTo, newsResponseTo == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
    }

    @PostMapping("/news")
    public ResponseEntity<NewsResponseTo> createNews(@RequestBody NewsRequestTo NewsTo) {
        NewsResponseTo addedNews = newsService.add(NewsTo);
        return new ResponseEntity<>(addedNews, HttpStatus.CREATED);
    }

    @DeleteMapping("/news/{id}")
    public ResponseEntity<NewsResponseTo> deleteNews(@PathVariable long id) {
        NewsResponseTo deletedNews = newsService.delete(id);
        return new ResponseEntity<>(deletedNews, deletedNews == null ? HttpStatus.NOT_FOUND : HttpStatus.NO_CONTENT);
    }

    @PutMapping("/news")
    public ResponseEntity<NewsResponseTo> updateNews(@RequestBody NewsRequestTo newsRequestTo) {
        NewsResponseTo newsResponseTo = newsService.update(newsRequestTo);
        return new ResponseEntity<>(newsResponseTo, newsResponseTo.getContent() == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
    }
}
