package com.bsuir.dc.dto.response;

import java.io.Serializable;

public class PostResponseTo implements Serializable {
    private long id;
    private long topicId;
    private String content;

    public void setId(long id) { this.id = id; }
    public long getId() { return id; }

    public void setTopicId(long topicId) { this.topicId = topicId; }
    public long getTopicId() { return topicId; }

    public void setContent(String content) { this.content = content; }
    public String getContent() { return content; }
}
