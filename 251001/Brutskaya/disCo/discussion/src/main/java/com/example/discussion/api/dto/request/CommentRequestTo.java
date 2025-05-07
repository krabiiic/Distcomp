package com.example.discussion.api.dto.request;

import com.example.discussion.model.enums.State;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class CommentRequestTo implements Serializable {

    private Long id;

    private Long issueId;

    @Size(min = 2, max = 2048)
    private String content;
    private String country;
    private State state;
}
