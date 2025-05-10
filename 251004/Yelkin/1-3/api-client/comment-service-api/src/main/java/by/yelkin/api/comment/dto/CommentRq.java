package by.yelkin.api.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentRq {
    @NotNull(message = "TopicId must not be null")
    private Long topicId;

    @NotBlank(message = "Content must not be blank")
    @Size(min = 2, max = 2048, message = "Content size must be between 2 and 64 chars")
    private String content;
}
