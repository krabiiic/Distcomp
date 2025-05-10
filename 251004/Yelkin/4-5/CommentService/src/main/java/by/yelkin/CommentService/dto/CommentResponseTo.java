package by.yelkin.CommentService.dto;

import lombok.Data;

@Data
public class CommentResponseTo {
    private Long topicId;
    private Long id;
    private String content;
}