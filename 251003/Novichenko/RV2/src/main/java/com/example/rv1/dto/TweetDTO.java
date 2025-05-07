package com.example.rv1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class TweetDTO {
    private int id;
    @JsonProperty("userId")
    private int userId;
    @JsonProperty("title")
    @Size(min = 2, max = 64, message = "Login must be between 2 and 64 characters")
    private String title;
    @JsonProperty("content")
    @Size(min = 4, max = 64, message = "Login must be between 2 and 64 characters")
    private String content;
}
