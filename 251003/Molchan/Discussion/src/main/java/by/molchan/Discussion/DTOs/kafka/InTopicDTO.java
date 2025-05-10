package by.molchan.Discussion.DTOs.kafka;


import by.molchan.Discussion.DTOs.Requests.CommentRequestDTO;

public class InTopicDTO {
    private String method;
    private CommentRequestDTO commentRequestDTO;
    private String status;

    public InTopicDTO(String method, CommentRequestDTO commentRequestDTO, String status) {
        this.method = method;
        this.commentRequestDTO = commentRequestDTO;
        this.status = status;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public CommentRequestDTO getCommentRequestDTO() {
        return commentRequestDTO;
    }

    public void setCommentRequestDTO(CommentRequestDTO commentRequestDTO) {
        this.commentRequestDTO = commentRequestDTO;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public InTopicDTO() {
    }
}
