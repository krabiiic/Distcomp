package by.yelkin.TopicService.dto.topic;

import by.yelkin.TopicService.entity.Comment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class TopicResponseTo {
    private Long id;
    private Long creatorId;
    @NotBlank(message = "Title cannot be blank")
    @Size(min = 2, max = 64, message = "Title must be between 2 and 64 characters")
    private String title;
    private String content;
    private LocalDateTime created;
    private LocalDateTime modified;
    private List<String> marks;
    private List<Comment> comments;
}
