package com.example.lab1.dto;

import com.example.lab1.model.State;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ReactionRequestTo {
    private String country = "default";
    
    @NotNull
    private Long issueId;
    
    private Long id;
    
    @NotNull
    @Size(min = 2, max = 2048)
    private String content;
    
    private State status;

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
    public State getStatus() {
        return status;
    }
    public void setStatus(State status) {
        this.status = status;
    }
}
