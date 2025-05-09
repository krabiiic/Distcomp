package by.yelkin.TopicService.controller;

import by.yelkin.TopicService.dto.CommentRequestTo;
import by.yelkin.TopicService.dto.CommentResponseTo;
import by.yelkin.TopicService.service.Impl.DiscussionClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1.0/comments")
public class PublisherController {

    private final DiscussionClient discussionClient;

    public PublisherController(DiscussionClient discussionClient) {
        this.discussionClient = discussionClient;
    }

    @GetMapping
    public ResponseEntity<List<CommentResponseTo>> getAllComments() {
        List<CommentResponseTo> posts = discussionClient.getAllComments();
        return ResponseEntity.ok(posts);
    }

    @PostMapping
    public ResponseEntity<CommentResponseTo> createComment(@RequestBody CommentRequestTo requestTo) {
        CommentResponseTo responseTo = discussionClient.createComment(requestTo);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(responseTo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentResponseTo> getCommentById(@PathVariable Long id) {
        try {
            CommentResponseTo responseTo = discussionClient.getCommentById(id);
            return ResponseEntity.ok(responseTo);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping
    public ResponseEntity<CommentResponseTo> updateComment(
            @RequestBody CommentRequestTo requestTo) {

        CommentResponseTo responseTo = discussionClient.processCommentRequest("PUT", requestTo);
        return ResponseEntity.ok(responseTo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        CommentRequestTo request = new CommentRequestTo();
        request.setId(id);
        discussionClient.processCommentRequest("DELETE", request);
        return ResponseEntity.noContent().build();
    }
}