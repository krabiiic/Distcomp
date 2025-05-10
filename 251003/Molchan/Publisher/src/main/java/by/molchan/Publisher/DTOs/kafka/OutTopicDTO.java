package by.molchan.Publisher.DTOs.kafka;

import by.molchan.Publisher.DTOs.Requests.CommentRequestDTO;
import by.molchan.Publisher.DTOs.Responses.CommentResponseDTO;
import org.springframework.http.HttpMethod;

import java.util.List;

public class OutTopicDTO {
    private CommentResponseDTO commentResponseDTO;
    private List<CommentResponseDTO> commentResponsesListDTO;
    private String status;
    private String error;

    public CommentResponseDTO getCommentResponseDTO() {
        return commentResponseDTO;
    }

    public void setCommentResponseDTO(CommentResponseDTO commentResponseDTO) {
        this.commentResponseDTO = commentResponseDTO;
    }

    public List<CommentResponseDTO> getCommentResponsesListDTO() {
        return commentResponsesListDTO;
    }

    public void setCommentResponsesListDTO(List<CommentResponseDTO> commentResponsesListDTO) {
        this.commentResponsesListDTO = commentResponsesListDTO;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public OutTopicDTO() {
    }
}
