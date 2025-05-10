package com.example.controller;

import com.example.dto.PostRequestTo;
import com.example.dto.PostResponseTo;
import com.example.service.PostService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1.0/posts")
public class PostController {

    private final PostService reactionService;

    public PostController(PostService reactionService) {
        this.reactionService = reactionService;
    }

    @PostMapping
    public ResponseEntity<PostResponseTo> createReaction(@Valid @RequestBody PostRequestTo request) {
        PostResponseTo response = reactionService.createReaction(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseTo> getReaction(@PathVariable Long id) {
        PostResponseTo response = reactionService.getReaction(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<PostResponseTo>> getAllReactions() {
        List<PostResponseTo> response = reactionService.getAllReactions();
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<PostResponseTo> updateReaction(@Valid @RequestBody PostRequestTo request) {
        if (request.getId() == null) {
            throw new IllegalArgumentException("ID must be provided");
        }
        PostResponseTo response = reactionService.updateReaction(request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReaction(@PathVariable Long id) {
        reactionService.deleteReaction(id);
        return ResponseEntity.noContent().build();
    }
}
