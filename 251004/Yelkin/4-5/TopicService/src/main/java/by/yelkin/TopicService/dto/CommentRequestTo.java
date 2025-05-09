package by.yelkin.TopicService.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentRequestTo {
    private Long id;
    @Size(min = 2, max = 2048, message = "Content must be between 2 and 2048 characters")
    private String content;
    private Long topicId;

    public CommentRequestTo(Long id) {
        this.id = id;
    }
}
