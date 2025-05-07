package by.ryabchikov.tweet_service.dto.mark;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record MarkUpdateRequestTo(
        @NotNull(message = "ID should not be null.")
        @Positive(message = "ID should be a positive number.")
        Long id,

        @NotBlank(message = "Name should not be blank.")
        @Size(min = 2, max = 32, message = "Name size should be between 2 and 32 chars.")
        String name
) {
}
