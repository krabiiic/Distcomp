package com.example.publisher.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ReactionRequestTo(
        String country,
        Long id,

        @NotNull(message = "Topic ID cannot be null")
        Long newsId,

        @NotBlank(message = "Content cannot be empty")
        @Size(min = 2, max = 2048, message = "Content must be 2 - 2048 characters long")
        String content
) {
}
