package by.yelkin.CommentService.dto;

import lombok.Data;

@Data
public class InTopicDTO {
    private String method;
    private CommentRequestTo commentRequestTo;
    private String status;
}
