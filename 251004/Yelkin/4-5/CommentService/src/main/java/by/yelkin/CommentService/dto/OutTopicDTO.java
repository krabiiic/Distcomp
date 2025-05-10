package by.yelkin.CommentService.dto;

import lombok.Data;

import java.util.List;

@Data
public class OutTopicDTO {
    private CommentResponseTo commentResponseTo;
    private List<CommentResponseTo> commentResponseTos;
    private String status;
    private String error;

    public OutTopicDTO(CommentResponseTo commentResponseTo, String status) {
        this.commentResponseTo = commentResponseTo;
        this.status = status;
    }

    public OutTopicDTO(List<CommentResponseTo> commentResponseTos, String status) {
        this.commentResponseTos = commentResponseTos;
        this.status = status;
    }

    public OutTopicDTO(String s, String decline) {
    }

    public OutTopicDTO(CommentResponseTo commentResponseTo, List<CommentResponseTo> commentResponseTos, String status, String error) {
        this.commentResponseTo = commentResponseTo;
        this.commentResponseTos = commentResponseTos;
        this.status = status;
        this.error = error;
    }

    public OutTopicDTO(CommentResponseTo commentResponseTo) {
        this.commentResponseTo = commentResponseTo;
    }

    public OutTopicDTO(List<CommentResponseTo> commentResponseTos) {
        this.commentResponseTos = commentResponseTos;
    }

    public OutTopicDTO(String status) {
        this.status = status;
    }
}