package by.yelkin.TopicService.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentUpdate {
    private Long id;
    private Long topicId;
    @Size(min = 2, max = 2048, message = "Content must be between 2 and 2048 characters")
    private String content;
}
