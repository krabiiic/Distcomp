package com.example.publisher.producer;

import com.example.discussion.api.dto.response.CommentResponseTo;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentProducer {
    private final KafkaTemplate<String, CommentResponseTo> kafkaTemplate;

    public void sendComment(CommentResponseTo response) {
        String key = String.valueOf(response.getIssueId());
        kafkaTemplate.send("InTopic", key, response);
    }
}
