package com.example.discussion.dto;

import com.example.discussion.state.ReactionState;

public record ReactionResponseTo(
        String country,
        Long id,
        Long topicId,
        String content,

        ReactionState state
) {
}
