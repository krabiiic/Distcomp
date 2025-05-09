package by.yelkin.commentservice.service;


import by.yelkin.api.comment.dto.CommentRq;
import by.yelkin.api.comment.dto.CommentRs;
import by.yelkin.api.comment.dto.CommentUpdateRq;
import by.yelkin.api.topic.client.TopicApiClient;
import by.yelkin.apihandler.exception.ApiError;
import by.yelkin.apihandler.exception.ApiException;
import by.yelkin.commentservice.entity.Comment;
import by.yelkin.commentservice.mapping.CommentMapper;
import by.yelkin.commentservice.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final TopicApiClient topicApiClient;

    @Transactional
    public CommentRs create(CommentRq rq) {
        if (!topicApiClient.readById(rq.getTopicId()).getStatusCode().is2xxSuccessful()) {
            throw new ApiException(ApiError.ERR_TOPIC_NOT_FOUND, rq.getTopicId().toString());
        }

        Comment comment = commentMapper.fromDto(rq);
        comment.setCountry("Default");
        comment.setId((long) (Math.random() * 10000000000L));

        return commentMapper.toDto(commentRepository.save(comment));
    }

    @Transactional
    public CommentRs readById(Long id) {
        return commentMapper.toDto(commentRepository.findById(id)
                .orElseThrow(() -> new ApiException(ApiError.ERR_COMMENT_NOT_FOUND, id.toString())));
    }

    @Transactional
    public List<CommentRs> readAll() {
        return commentMapper.toDtoList(commentRepository.findAll());
    }

    @Transactional
    public CommentRs update(CommentUpdateRq rq) {
        var comment = commentRepository.findById(rq.getId())
                .orElseThrow(() -> new ApiException(ApiError.ERR_COMMENT_NOT_FOUND, rq.getId().toString()));

        commentMapper.updateComment(comment, rq);

        return commentMapper.toDto(commentRepository.save(comment));
    }

    @Transactional
    public void deleteById(Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new ApiException(ApiError.ERR_COMMENT_NOT_FOUND));
        commentRepository.delete(comment);
    }
}
