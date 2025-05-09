package com.example.publisher.dto;

import com.example.publisher.state.ReactionState;

public record ReactionResponseTo(
        String country,
        Long id,
        Long topicId,
        String content,
        ReactionState state
) {
}
