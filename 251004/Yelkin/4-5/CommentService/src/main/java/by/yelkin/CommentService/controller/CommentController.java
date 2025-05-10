package by.yelkin.CommentService.controller;

import by.yelkin.CommentService.dto.CommentRequestTo;
import by.yelkin.CommentService.dto.CommentResponseTo;
import by.yelkin.CommentService.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1.0/comments")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }


    @GetMapping
    public ResponseEntity<List<CommentResponseTo>> getAllComments() {
        List<CommentResponseTo> posts = commentService.findAll();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentResponseTo> getCommentsById(@PathVariable Long id) {

        CommentResponseTo responseTo = commentService.findById(id);
        return ResponseEntity.ok(responseTo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentResponseTo> update(@PathVariable(required = false) Long id, @RequestBody CommentRequestTo commentRequestTo) {
        if (id != null) {
            commentRequestTo.setId(id);
        }
        return ResponseEntity.ok(commentService.update(commentRequestTo));
    }

}