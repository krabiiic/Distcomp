package by.ryabchikov.tweet_service.dto.comment;

import lombok.Builder;

@Builder
public record CommentResponseTo(
        Long id,
        Long tweetId,
        String content
) {
}
