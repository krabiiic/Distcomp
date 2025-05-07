package com.example.publisher.client;

import com.example.discussion.api.dto.request.CommentRequestTo;
import com.example.discussion.api.dto.response.CommentResponseTo;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class DiscussionClient {
    private final RestClient restClient = RestClient.create("http://localhost:24130");

    public CommentResponseTo createComment(CommentRequestTo request) {
        return restClient.post()
                .uri("/api/v1.0/comments")
                .body(request)
                .retrieve()
                .body(CommentResponseTo.class);
    }

    public List<CommentResponseTo> getAllComments() {
        return restClient.get()
                .uri("/api/v1.0/comments")
                .retrieve()
                .body(List.class);
    }

    public CommentResponseTo getById(Long id) {
        return restClient.get()
                .uri("/api/v1.0/comments/{id}", id)
                .retrieve()
                .body(CommentResponseTo.class);
    }

    public CommentResponseTo update(CommentRequestTo request) {
        return restClient.put()
                .uri("/api/v1.0/comments")
                .body(request)
                .retrieve()
                .body(CommentResponseTo.class);
    }

    public void delete(Long id) {
        restClient.delete()
                .uri("/api/v1.0/comments/{id}", id)
                .retrieve()
                .body(Void.class);
    }
}
