package by.ryabchikov.tweet_service.dto.tweet;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record TweetUpdateRequestTo(
        @NotNull(message = "ID should not be null.")
        @Positive(message = "ID should be a positive number.")
        Long id,

        @NotNull(message = "Creator-ID should not be null.")
        @Positive(message = "Creator-ID should be a positive number.")
        Long creatorId,

        @NotBlank(message = "Title should not be blank.")
        @Size(min = 2, max = 64, message = "Title size should be between 2 and 64 chars.")
        String title,

        @NotBlank(message = "Content should not be blank.")
        @Size(min = 4, max = 2048, message = "Content size should be between 4 and 2048 chars.")
        String content
) {
}
