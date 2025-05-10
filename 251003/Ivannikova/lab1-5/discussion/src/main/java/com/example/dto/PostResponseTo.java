package com.example.dto;

import com.example.model.Post;

public class PostResponseTo {
    private String country;
    private Long issueId;
    private Long id;
    private String content;
    private Post.State state;

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
    public Post.State getState() {
        return state;
    }
    public void setState(Post.State state) {
        this.state = state;
    }
}