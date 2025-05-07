package com.example.lab1.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class KafkaReactionMessage {
    public enum OperationType {
        CREATE,
        READ,
        UPDATE,
        DELETE
    }

    public enum State {
        PENDING,
        APPROVE,
        DECLINE
    }

    private OperationType operationType;
    private String country;
    private Long issueId;
    private Long id;
    private String content;
    private State state;

     public KafkaReactionMessage() {}

    public OperationType getOperationType() {
        return operationType;
    }
    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }
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
    public State getState() {
        return state;
    }
    public void setState(State state) {
        this.state = state;
    }
}