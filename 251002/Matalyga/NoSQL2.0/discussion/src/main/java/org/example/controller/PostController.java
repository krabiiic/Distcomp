package org.example.controller;

import jakarta.validation.Valid;
import org.example.dto.PostRequestTo;
import org.example.dto.PostResponseTo;
import org.example.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/v1.0/posts")
public class PostController {
    @Autowired
    PostService postService;
    private static final Logger logger = LoggerFactory.getLogger(PostController.class);
    @GetMapping
    public List<PostResponseTo> getPosts() {
        return postService.getPosts();
    }

    @GetMapping("/{id}")
    public PostResponseTo getPost(@PathVariable Long id) {
        return postService.getPostById(id);
    }

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable Long id) {
        postService.deletePost(id);
    }

    @PostMapping
    public PostResponseTo savePost(@RequestHeader("Accept-Language") String acceptLanguageHeader, @RequestBody PostRequestTo post) {
        logger.info("Получен запрос на сохранение поста: {}", post);

        PostResponseTo response = postService.savePost(post, acceptLanguageHeader);

        logger.info("Возвращаемый ответ: {}", response);

        return response;
    }

    @PutMapping()
    public PostResponseTo updatePost(@RequestHeader("Accept-Language") String acceptLanguageHeader, @RequestBody PostRequestTo post) {
        return postService.updatePost(post, acceptLanguageHeader);
    }

    @GetMapping("/byIssue/{id}")
    public List<PostResponseTo> getEditorByIssueId(@PathVariable Long id) {
        return postService.getPostByIssueId(id);
    }
}
