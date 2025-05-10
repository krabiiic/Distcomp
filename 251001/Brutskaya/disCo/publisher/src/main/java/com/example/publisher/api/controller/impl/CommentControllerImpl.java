package com.example.publisher.api.controller.impl;

import com.example.discussion.api.dto.request.CommentRequestTo;
import com.example.discussion.api.dto.response.CommentResponseTo;
import com.example.publisher.api.controller.CommentController;
import com.example.publisher.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CommentControllerImpl implements CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentControllerImpl(CommentService commentService) {
        this.commentService = commentService;
    }

    @Override
    public CommentResponseTo create(CommentRequestTo request) {
        return commentService.create(request);
    }

    @Override
    public List<CommentResponseTo> getAll() {
        return commentService.getAll();
    }

    @Override
    public CommentResponseTo getById(Long id) {
        return commentService.getById(id);
    }

    @Override
    public CommentResponseTo update(CommentRequestTo request) {
        return commentService.update(request);
    }

    @Override
    public void delete(Long id) {
        commentService.delete(id);
    }
}
