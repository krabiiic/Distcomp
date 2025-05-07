package com.example.publisher.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequestTo(
        Long id,

        @NotBlank(message = "Login cannot be empty")
        @Size(min = 2, max = 64, message = "Login must be between 2 and 64 characters")
        String login,

        @NotBlank(message = "Password cannot be empty")
        @Size(min = 8, max = 128, message = "Password must be between 8 and 128 characters")
        String password,

        @NotBlank(message = "Firstname cannot be empty")
        @Size(min = 2, max = 64, message = "Firstname must be between 2 and 64 characters")
        String firstname,

        @NotBlank(message = "Lastname cannot be empty")
        @Size(min = 2, max = 64, message = "Lastname must be between 2 and 64 characters")
        String lastname
) {
}
