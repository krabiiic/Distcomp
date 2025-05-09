package by.yelkin.TopicService.service;


import by.yelkin.TopicService.repository.TopicRepository;
import by.yelkin.api.comment.client.CommentApiClient;
import by.yelkin.api.comment.dto.CommentRq;
import by.yelkin.api.comment.dto.CommentRs;
import by.yelkin.api.comment.dto.CommentUpdateRq;
import by.yelkin.apihandler.exception.ApiError;
import by.yelkin.apihandler.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final TopicRepository topicRepository;
    private final CommentApiClient commentApiClient;

    @Transactional
    public CommentRs create(CommentRq rq) {
        if (topicRepository.findById(rq.getTopicId()).isEmpty()) {
            throw new ApiException(ApiError.ERR_TOPIC_NOT_FOUND, rq.getTopicId().toString());
        }
        return commentApiClient.create(rq).getBody();
    }

    @Transactional
    public CommentRs readById(Long id) {
        return commentApiClient.readById(id).getBody();
    }

    @Transactional
    public List<CommentRs> readAll() {
        return commentApiClient.readAll().getBody();
    }

    @Transactional
    public CommentRs update(CommentUpdateRq rq) {
        return commentApiClient.update(rq).getBody();
    }

    @Transactional
    public void deleteById(Long id) {
        commentApiClient.deleteById(id);
    }
}
