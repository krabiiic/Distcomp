package com.example.rv1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO {
    private int id;
    @JsonProperty("newsId")
    private int newsId;
    @JsonProperty("content")
    @Size(min = 2, max = 2048, message = "Login must be between 2 and 2048 characters")
    private String content;
}
