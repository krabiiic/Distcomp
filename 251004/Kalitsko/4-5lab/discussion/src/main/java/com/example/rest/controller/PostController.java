package com.example.rest.controller;

import com.example.rest.dto.OutTopicDTO;
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


    @GetMapping
    public ResponseEntity<List<PostResponseTo>> getAllPosts() {
        List<PostResponseTo> posts = postService.findAll();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseTo> getPostById(@PathVariable Long id) {

        PostResponseTo responseTo = postService.findById(id);
        return ResponseEntity.ok(responseTo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostResponseTo> update(@PathVariable(required = false) Long id, @RequestBody PostRequestTo  postRequestTo) {
        if (id != null) {
            postRequestTo.setId(id);
        }
        return ResponseEntity.ok(postService.update(postRequestTo));
    }

}