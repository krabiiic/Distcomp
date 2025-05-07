package com.example.discussion.api.dto.response;

import com.example.discussion.model.enums.State;
import lombok.Data;

import java.io.Serializable;

@Data
public class CommentResponseTo implements Serializable {

    private Long id;
    private Long issueId;
    private String content;
    private String country;
    private State state;
}
