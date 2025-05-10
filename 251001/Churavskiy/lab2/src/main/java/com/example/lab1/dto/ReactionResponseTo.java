package com.example.lab1.dto;

public record ReactionResponseTo(
        Long id,
        Long topicId,
        String content
) {
}
