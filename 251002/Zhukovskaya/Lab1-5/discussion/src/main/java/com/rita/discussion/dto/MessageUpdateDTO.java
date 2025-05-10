package com.rita.discussion.dto;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record MessageUpdateDTO(@NotNull Long id,
                               @NotNull(groups = UpdateGroup.class) Long tweetId,
                               @NotNull(groups = UpdateGroup.class) @Length(min = 2, max = 2048) String content) {

}
