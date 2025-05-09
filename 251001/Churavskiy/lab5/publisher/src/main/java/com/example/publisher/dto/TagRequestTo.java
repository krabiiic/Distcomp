package com.example.publisher.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TagRequestTo(
        Long id,

        @NotBlank(message = "Tag name cannot be empty")
        @Size(min = 2, max = 32, message = "Tag name must be between 2 and 32 characters")
        String name
) {
}
