package com.rita.publisher.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

@Schema(description = "Сущность TweetCreateDTO")
public record TweetCreateDTO(@NotNull @Schema(description = "Уникальный идентификатор пользователя", example = "1")
Long writerId, @NotNull @Length(min = 2, max = 64) @Schema(description = "Заголовок", example = "Title") String title, @NotNull @Length(min = 4, max = 2048) @Schema(description = "Содержимое", example = "Content") String content,
                             Set<String> stickers) {
}
