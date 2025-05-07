package com.example.rest.dto;

import jakarta.validation.constraints.Size;

public class PostRequestTo {
    private Long id;
    @Size(min = 2, max = 2048, message = "Content must be between 2 and 2048 characters")
    private String content;
    private Long topicId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public PostRequestTo() {
    }

    public PostRequestTo(Long id) {
        this.id = id;
    }
}
