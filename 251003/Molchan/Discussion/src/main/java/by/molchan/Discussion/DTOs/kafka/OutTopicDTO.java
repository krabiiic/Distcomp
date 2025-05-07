package by.molchan.Discussion.DTOs.kafka;


import by.molchan.Discussion.DTOs.Responses.CommentResponseDTO;

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

    public OutTopicDTO(CommentResponseDTO commentResponseDTO, String status) {
        this.commentResponseDTO = commentResponseDTO;
        this.status = status;
    }

    public OutTopicDTO( String error, String status) {
        this.status = status;
        this.error = error;
    }

    public OutTopicDTO(List<CommentResponseDTO> commentResponsesListDTO, String status) {
        this.status = status;
        this.commentResponsesListDTO = commentResponsesListDTO;
    }

    public List<CommentResponseDTO> getCommentResponsesListDTO() {
        return commentResponsesListDTO;
    }

    public void setCommentResponsesListDTO(List<CommentResponseDTO> commentResponsesListDTO) {
        this.commentResponsesListDTO = commentResponsesListDTO;
    }

    public OutTopicDTO() {
    }
}
