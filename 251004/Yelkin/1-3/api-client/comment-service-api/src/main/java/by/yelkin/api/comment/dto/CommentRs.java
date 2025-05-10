package by.yelkin.api.comment.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentRs {
    private Long id;
    private Long topicId;
    private String content;
}
