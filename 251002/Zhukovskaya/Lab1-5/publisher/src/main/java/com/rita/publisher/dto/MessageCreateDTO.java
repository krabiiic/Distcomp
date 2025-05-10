package com.rita.publisher.dto;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record MessageCreateDTO(@NotNull Long tweetId,
                               @NotNull @Length(min = 2, max = 2048) String content) {
}
