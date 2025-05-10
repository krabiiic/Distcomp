package by.yelkin.TopicService.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentResponseTo {
    private Long id;
    private Long topicId;
    private String content;
}
