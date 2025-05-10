package com.example.lab1.dto;

public class ReactionResponseTo {
    private String country;
    private Long issueId;
    private Long id;
    private String content;
    private KafkaPostMessage.State state;

    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public Long getIssueId() {
        return issueId;
    }
    public void setIssueId(Long issueId) {
        this.issueId = issueId;
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
    public KafkaPostMessage.State getState() {
        return state;
    }
    public void setState(KafkaPostMessage.State state) {
        this.state = state;
    }
}