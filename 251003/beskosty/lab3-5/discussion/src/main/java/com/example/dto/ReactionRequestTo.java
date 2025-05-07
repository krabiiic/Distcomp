package com.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ReactionRequestTo {
    @NotBlank
    private String country = "default";

    @NotNull
    private Long issueId;
    
    @NotNull
    private Long id;
    
    @NotBlank
    @Size(min = 2, max = 2048)
    private String content;

    private String status;

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
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
