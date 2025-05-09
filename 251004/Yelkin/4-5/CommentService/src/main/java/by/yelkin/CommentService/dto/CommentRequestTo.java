package by.yelkin.CommentService.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentRequestTo {
    private Long id;

    @NotNull(message = "topicId should not be null.")
    @Positive(message = "topicId should be a positive number.")
    private Long topicId;

    @NotBlank(message = "Content should not be blank.")
    @Size(min = 2, max = 2048, message = "Content must be between 2 and 2048 characters")
    private String content;
}