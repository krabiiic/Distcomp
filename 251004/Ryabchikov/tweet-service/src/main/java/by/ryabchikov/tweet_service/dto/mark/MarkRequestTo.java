package by.ryabchikov.tweet_service.dto.mark;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record MarkRequestTo(
        @NotBlank(message = "Name should not be blank.")
        @Size(min = 2, max = 32, message = "Name size should be between 2 and 32 chars.")
        String name
) {
}
