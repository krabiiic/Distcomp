package com.example.rest.dto;


public class PostResponseTo {
    private Long topicId;
    private Long id;
    private String content;

    // Constructor
    public PostResponseTo(Long topicId, Long id, String content) {
        this.topicId = topicId;
        this.id = id;
        this.content = content;
    }

    // Getters and Setters
    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

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
}