package com.example.publisher.consumer;

import com.example.discussion.api.dto.response.CommentResponseTo;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentConsumer {

    @KafkaListener(topics = "OutTopic", groupId = "discussion-group")
    public CommentResponseTo listen(CommentResponseTo response) {
        return response;
    }
}
