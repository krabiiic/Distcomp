package com.example.publisher.dto;

public record ReactionResponseTo(
        String country,
        Long id,
        Long topicId,
        String content
) {
}
