package com.example.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class PostRequestTo {
    private Long id;

    @NotNull(message = "topicId should not be null.")
    @Positive(message = "topicId should be a positive number.")
    private Long topicId;

    @NotBlank(message = "Content should not be blank.")
    @Size(min = 2, max = 2048, message = "Content must be between 2 and 2048 characters")
    private String content;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}