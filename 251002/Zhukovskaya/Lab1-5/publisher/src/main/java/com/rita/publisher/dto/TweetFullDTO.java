package com.rita.publisher.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.Set;

@Schema(description = "Сущность TweetFullDTO")
public record TweetFullDTO(@Schema(description = "Уникальный идентификатор твита", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
                           Long id, @Schema(description = "Уникальный пользователя", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
                            TweetFullDTOWriter writer, @Schema(description = "Заголовок", example = "Title", accessMode = Schema.AccessMode.READ_ONLY)
                           String title, @Schema(description = "Содержимое", example = "Content", accessMode = Schema.AccessMode.READ_ONLY)
                           String content, @Schema(description = "Дата создания", example = "2025-01-01T09:49:19.275039200", accessMode = Schema.AccessMode.READ_ONLY)
                           LocalDateTime createdTime, @Schema(description = "Дата создания", example = "2025-01-02T09:52:30.275039200", accessMode = Schema.AccessMode.READ_ONLY)
                           LocalDateTime modifiedTime,@Schema(description = "Список сообщений к твиту", example = "1", accessMode = Schema.AccessMode.READ_ONLY) Set<TweetFullDTOMessage> messages,
                           @Schema(description = "Список стикеров к твиту", example = "1", accessMode = Schema.AccessMode.READ_ONLY) Set<TweetFullDTOSticker> stickers) {
    public record TweetFullDTOWriter(Long id,String name){

}
    public record TweetFullDTOSticker(Long id,String name){

}
    public record TweetFullDTOMessage(Long id, String content){

}
}
