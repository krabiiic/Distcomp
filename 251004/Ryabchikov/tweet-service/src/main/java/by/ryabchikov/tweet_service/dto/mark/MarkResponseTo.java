package by.ryabchikov.tweet_service.dto.mark;

import lombok.Builder;

@Builder
public record MarkResponseTo(
        Long id,
        String name
) {
}
