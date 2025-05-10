package com.example.dto;

import com.example.model.Reaction;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class KafkaReactionMessage {
    public enum OperationType {
        CREATE,
        READ,
        UPDATE,
        DELETE
    }

    private OperationType operationType;
    private String country;
    private Long issueId;
    private Long id;
    private String content;
    private Reaction.State state;

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
    public Reaction.State getState() {
        return state;
    }
    public void setState(Reaction.State state) {
        this.state = state;
    }
}