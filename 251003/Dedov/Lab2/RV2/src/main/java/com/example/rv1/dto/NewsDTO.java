package com.example.rv1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class NewsDTO {
    private int id;
    @JsonProperty("editorId")
    private int editorId;
    @JsonProperty("title")
    @Size(min = 2, max = 64, message = "Title must be between 2 and 64 characters")
    private String title;
    @JsonProperty("content")
    @Size(min = 4, max = 2048, message = "Content must be between 4 and 2048 characters")
    private String content;
}
