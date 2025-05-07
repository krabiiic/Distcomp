package com.example.publisher.service;

import com.example.discussion.api.dto.request.CommentRequestTo;
import com.example.discussion.api.dto.response.CommentResponseTo;
import com.example.discussion.model.enums.State;
import com.example.publisher.client.DiscussionClient;
import com.example.publisher.producer.CommentProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final DiscussionClient discussionClient;
    private final CommentProducer commentProducer;

    @Caching(
            put = @CachePut(value = "comments", key = "#result.id"),
            evict = @CacheEvict(value = "commentsList", allEntries = true)
    )
    public CommentResponseTo create(CommentRequestTo request) {
        request.setState(State.PENDING);
        CommentResponseTo response = discussionClient.createComment(request);
        commentProducer.sendComment(response);
        return response;
    }

    @Cacheable(value = "commentsList")
    public List<CommentResponseTo> getAll() {
        return discussionClient.getAllComments();
    }

    @Cacheable(value = "comments", key = "#id")
    public CommentResponseTo getById(Long id) {
        return discussionClient.getById(id);
    }

    @Caching(
            put = @CachePut(value = "comments", key = "#request.id"),
            evict = @CacheEvict(value = "commentsList", allEntries = true)
    )
    public CommentResponseTo update(CommentRequestTo request) {
        return discussionClient.update(request);
    }

    @Caching(
            evict = {
                    @CacheEvict(value = "comments", key = "#id"),
                    @CacheEvict(value = "commentsList", allEntries = true)
            }
    )
    public void delete(Long id) {
        discussionClient.delete(id);
    }
}
