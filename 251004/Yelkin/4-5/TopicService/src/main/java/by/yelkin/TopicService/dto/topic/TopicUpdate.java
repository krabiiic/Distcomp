package by.yelkin.TopicService.dto.topic;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TopicUpdate {
    private Long id;
    private Long creatorId;
    @NotBlank(message = "Title cannot be blank")
    @Size(min = 2, max = 64, message = "Title must be between 2 and 64 characters")
    private String title;
    @Size(min = 4, max = 2048, message = "Content must be between 4 and 2048 characters")
    private String content;
}
