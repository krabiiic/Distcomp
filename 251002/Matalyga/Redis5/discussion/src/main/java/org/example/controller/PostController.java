package org.example.controller;

import jakarta.validation.Valid;
import org.example.dto.PostRequestTo;
import org.example.dto.PostResponseTo;
import org.example.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1.0/posts")
public class PostController {
    @Autowired
    PostService postService;
    @GetMapping
    public List<PostResponseTo> getPosts() {
        return postService.getPosts();
    }

    @GetMapping("/{id}")
    public PostResponseTo getPost(@PathVariable Long id) {
        return postService.getPostById(id);
    }

    @DeleteMapping("/{id}")
    public PostResponseTo deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return new PostResponseTo();
    }

    @PostMapping
    public PostResponseTo savePost(@RequestHeader("Accept-Language") String acceptLanguageHeader, @RequestBody PostRequestTo post) {
        System.out.println("---SAVE POST");
        PostResponseTo response = postService.savePost(post, acceptLanguageHeader);
        return response;
    }

    @PutMapping()
    public PostResponseTo updatePost(@RequestHeader("Accept-Language") String acceptLanguageHeader, @RequestBody PostRequestTo post) {
        return postService.updatePost(post, acceptLanguageHeader);
    }

    @GetMapping("/byNews/{id}")
    public List<PostResponseTo> getEditorByNewsId(@PathVariable Long id) {
        return postService.getPostByIssueId(id);
    }
}
