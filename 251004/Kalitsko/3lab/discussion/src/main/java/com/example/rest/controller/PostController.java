package com.example.rest.controller;

import com.example.rest.dto.PostRequestTo;
import com.example.rest.dto.PostResponseTo;
import com.example.rest.dto.PostUpdate;
import com.example.rest.service.Impl.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1.0/posts")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    // 1. Создание нового поста
    @PostMapping
    public ResponseEntity<PostResponseTo> createPost(@RequestBody PostRequestTo requestTo) {
        PostResponseTo responseTo = postService.createPost(requestTo);
        return ResponseEntity.ok(responseTo);
    }

    // 2. Получение всех постов
    @GetMapping
    public ResponseEntity<List<PostResponseTo>> getAllPosts() {
        List<PostResponseTo> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    // 3. Получение поста по ключу
    @GetMapping("{id}")
    public ResponseEntity<PostResponseTo> getPostById(@PathVariable Long id) {

        PostResponseTo responseTo = postService.getPostById(id);
        return ResponseEntity.ok(responseTo);
    }

    // 4. Обновление поста
    @PutMapping
    public ResponseEntity<PostResponseTo> updatePost(@Valid @RequestBody PostUpdate postUpdate) {

        PostResponseTo responseTo = postService.updatePost(postUpdate);
        return ResponseEntity.ok(responseTo);
    }

    // 5. Удаление поста
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {

        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}