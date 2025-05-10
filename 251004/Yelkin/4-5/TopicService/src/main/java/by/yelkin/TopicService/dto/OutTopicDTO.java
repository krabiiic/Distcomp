package by.yelkin.TopicService.dto;

import lombok.Data;

import java.util.List;

@Data
public class OutTopicDTO {
    private CommentResponseTo commentResponseTo;
    private List<CommentResponseTo> commentResponseTos;
    private String status;
    private String error;
}