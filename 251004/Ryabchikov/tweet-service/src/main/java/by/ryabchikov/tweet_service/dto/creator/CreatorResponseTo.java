package by.ryabchikov.tweet_service.dto.creator;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
public record CreatorResponseTo (
        Long id,
        String login,
        String password,
        String firstname,
        String lastname
) {
}

