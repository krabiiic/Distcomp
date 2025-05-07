package com.bsuir.discussion.dto.responses;

public class PostResponseDTO {
    private long id;

    private long topicId;

    private String content;

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getTopicId() { return topicId; }
    public void setTopicId(long topicId) { this.topicId = topicId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}