package com.example.discussion.api.controller;

import com.example.discussion.api.dto.request.CommentRequestTo;
import com.example.discussion.api.dto.response.CommentResponseTo;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface CommentController {

    @PostMapping("/api/v1.0/comments")
    @ResponseStatus(HttpStatus.CREATED)
    CommentResponseTo create(@RequestBody
                             @Valid CommentRequestTo request);

    @GetMapping("/api/v1.0/comments")
    @ResponseStatus(HttpStatus.OK)
    List<CommentResponseTo> getAll();

    @GetMapping("/api/v1.0/comments/{id}")
    @ResponseStatus(HttpStatus.OK)
    CommentResponseTo getById(@PathVariable Long id);

    @PutMapping("/api/v1.0/comments")
    @ResponseStatus(HttpStatus.OK)
    CommentResponseTo update(@RequestBody
                             @Valid CommentRequestTo request);

    @DeleteMapping("/api/v1.0/comments/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable Long id);
}
