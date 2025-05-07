package by.ryabchikov.tweet_service.dto.creator;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CreatorUpdateRequestTo(
        @NotNull(message = "ID should not be null.")
        @Positive(message = "ID should be a positive number.")
        Long id,

        @NotBlank(message = "Login should not be blank.")
        @Size(min = 2, max = 64, message = "Login size should be between 2 and 64 chars.")
        String login,

        @NotBlank(message = "Password should not be blank.")
        @Size(min = 8, max = 128, message = "Password size should be between 8 and 128 chars.")
        String password,

        @NotBlank(message = "Firstname should not be blank.")
        @Size(min = 2, max = 64, message = "Firstname size should be between 2 and 64 chars.")
        String firstname,

        @NotBlank(message = "Lastname should not be blank.")
        @Size(min = 2, max = 64, message = "Lastname size should be between 2 and 64 chars.")
        String lastname
) {
}
