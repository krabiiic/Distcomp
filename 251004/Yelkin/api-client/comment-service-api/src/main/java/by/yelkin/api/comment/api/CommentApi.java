package by.yelkin.api.comment.api;

import by.yelkin.api.comment.dto.CommentRq;
import by.yelkin.api.comment.dto.CommentRs;
import by.yelkin.api.comment.dto.CommentUpdateRq;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Validated
public interface CommentApi {
    @PostMapping("/api/v1.0/comments")
    ResponseEntity<CommentRs> create(@Valid @RequestBody CommentRq rq);

    @GetMapping("/api/v1.0/comments/{id}")
    ResponseEntity<CommentRs> readById(@Valid @NotNull @PathVariable("id") Long id);

    @GetMapping("/api/v1.0/comments")
    ResponseEntity<List<CommentRs>> readAll();

    @PutMapping("/api/v1.0/comments")
    ResponseEntity<CommentRs> update(@Valid @RequestBody CommentUpdateRq rq);

    @DeleteMapping("/api/v1.0/comments/{id}")
    ResponseEntity<Void> deleteById(@Valid @NotNull @PathVariable("id") Long id);
}
