package by.yelkin.TopicService.service;



import by.yelkin.TopicService.dto.CommentRequestTo;
import by.yelkin.TopicService.dto.CommentResponseTo;
import by.yelkin.TopicService.dto.CommentUpdate;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    CommentResponseTo create(CommentRequestTo post);

    CommentResponseTo update(CommentUpdate updatedPost);

    void deleteById(Long id);

    List<CommentResponseTo> findAll();

    Optional<CommentResponseTo> findById(Long id);
}
