package com.rita.discussion.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Сущность TweetDTO")
public record TweetDTO(@Schema(description = "Уникальный идентификатор твита", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
Long id, @Schema(description = "Уникальный пользователя", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
Long writerId, @Schema(description = "Заголовок", example = "Title", accessMode = Schema.AccessMode.READ_ONLY)
String title, @Schema(description = "Содержимое", example = "Content", accessMode = Schema.AccessMode.READ_ONLY)
String content, @Schema(description = "Дата создания", example = "2025-01-01T09:49:19.275039200", accessMode = Schema.AccessMode.READ_ONLY)
LocalDateTime createdTime, @Schema(description = "Дата создания", example = "2025-01-02T09:52:30.275039200", accessMode = Schema.AccessMode.READ_ONLY)
 LocalDateTime modifiedTime) {
}
