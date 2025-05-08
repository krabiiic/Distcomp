package com.example.lab1.dto;

import java.time.LocalDateTime;
import java.util.List;

public class IssueResponseTo {
    private Long id;
    private Long userId;
    private String title;
    private String content;
    private LocalDateTime created;
    private LocalDateTime modified;

    private List<String> marks;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public LocalDateTime getCreated() {
        return created;
    }
    public void setCreated(LocalDateTime created) {
        this.created = created;
    }
    public LocalDateTime getModified() {
        return modified;
    }
    public void setModified(LocalDateTime modified) {
        this.modified = modified;
    }
    public List<String> getMarks() {
        return marks;
    }
    public void setMarks(List<String> marks) {
        this.marks = marks;
    }

}
