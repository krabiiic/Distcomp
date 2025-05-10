package com.rita.publisher.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record TweetUpdateDTO(@NotNull @Schema(description = "Уникальный идентификатор твита", example = "1") Long id,
 @NotNull(groups = UpdateGroup.class) @Schema(description = "Уникальный пользователя", example = "1") Long writerId,
 @NotNull(groups = UpdateGroup.class) @Length(min = 2, max = 64,groups = UpdateGroup.class) @Schema(description = "Заголовок", example = "Title") String title,
 @NotNull(groups = UpdateGroup.class) @Length(min = 4, max = 2048,groups = UpdateGroup.class) @Schema(description = "Содержимое", example = "Content") String content) {
}
