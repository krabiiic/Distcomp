package by.ryabchikov.tweet_service.dto.tweet;

import lombok.Builder;

import java.time.LocalDateTime;


@Builder
public record TweetResponseTo(
        Long id,
        Long creatorId,
        String title,
        String content,
        LocalDateTime createdTime,
        LocalDateTime lastModifiedTime
) {
}
