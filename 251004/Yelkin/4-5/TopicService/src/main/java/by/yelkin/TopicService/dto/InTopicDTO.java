package by.yelkin.TopicService.dto;

import lombok.Data;

@Data
public class InTopicDTO {
    private String method;
    private CommentRequestTo commentRequestTo;
    private String status;

    public InTopicDTO(String method, CommentRequestTo commentRequestTo, String status) {
        this.method = method;
        this.commentRequestTo = commentRequestTo;
        this.status = status;
    }
}
