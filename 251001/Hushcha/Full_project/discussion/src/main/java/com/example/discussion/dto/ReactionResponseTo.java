package com.example.discussion.dto;

public record ReactionResponseTo(
        String country,
        Long id,
        Long newsId,
        String content
) {
}
