package by.yelkin.TopicService.controller;

import by.yelkin.api.comment.api.CommentApi;
import by.yelkin.api.comment.dto.CommentRq;
import by.yelkin.api.comment.dto.CommentRs;
import by.yelkin.api.comment.dto.CommentUpdateRq;
import by.yelkin.TopicService.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class CommentController implements CommentApi {
    private final CommentService commentService;

    @Override
    public ResponseEntity<CommentRs> create(CommentRq rq) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(commentService.create(rq));
    }

    @Override
    public ResponseEntity<CommentRs> readById(Long id) {
        return ResponseEntity.ok().body(commentService.readById(id));
    }

    @Override
    public ResponseEntity<List<CommentRs>> readAll() {
        return ResponseEntity.ok().body(commentService.readAll());
    }

    @Override
    public ResponseEntity<CommentRs> update(CommentUpdateRq rq) {
        return ResponseEntity.ok().body(commentService.update(rq));
    }

    @Override
    public ResponseEntity<Void> deleteById(Long id) {
        commentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}