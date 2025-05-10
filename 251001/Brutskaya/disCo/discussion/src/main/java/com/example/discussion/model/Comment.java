package com.example.discussion.model;

import com.example.discussion.model.enums.State;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@Document(collection = "tbl_comment")
@NoArgsConstructor
@AllArgsConstructor
public class Comment implements Serializable {

    @Transient
    public static final String SEQUENCE_NAME = "comments_sequence";

    @Id
    private Long id;
    private Long issueId;
    private String country;
    private String content;
    private State state;
}
