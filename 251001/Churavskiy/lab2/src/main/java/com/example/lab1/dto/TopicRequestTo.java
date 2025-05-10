package com.example.lab1.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Set;

public record TopicRequestTo(
        Long id,

        @NotBlank(message = "Title cannot be empty")
        @Size(min = 2, max = 64, message = "Title must be between 2 and 64 characters")
        String title,

        @NotBlank(message = "Content cannot be empty")
        @Size(min = 4, max = 2048, message = "Content must be at least 4 to 2048 characters long")
        String content,

        @NotNull(message = "User ID cannot be null")
        Long userId,

        Set<String> tags
) {
}
