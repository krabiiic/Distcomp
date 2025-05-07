package com.example.discussion.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection  = "tbl_note")
public class Note {

    public Note(){}

    public Note(String content, Long newsId){
        this.content = content;
        this.newsId = newsId;
    }
    @Id
    private Long id;

    private Long newsId;

    private String content;

    public Long getNewsId() {
        return newsId;
    }

    public void setNewsId(Long newsId) {
        this.newsId = newsId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}