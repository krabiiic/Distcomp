package com.example.discussion.consumer;

import com.example.discussion.api.dto.response.CommentResponseTo;
import com.example.discussion.model.Comment;
import com.example.discussion.model.enums.State;
import com.example.discussion.mupper.CommentMapper;
import com.example.discussion.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentConsumer {
    private final CommentService commentService;
    private final CommentMapper commentMapper;

    @KafkaListener(topics = "InTopic", groupId = "discussion-group")
    public void listen(CommentResponseTo response) {
        Comment comment = commentMapper.fromResponseToEntity(response);
        comment.setState(moderateComment(response.getContent()));
        commentService.update(commentMapper.fromEntityToRequest(comment));
    }


    private State moderateComment(String text) {
        return text.contains("сука") ? State.DECLINE : State.APPROVE;
    }
}
