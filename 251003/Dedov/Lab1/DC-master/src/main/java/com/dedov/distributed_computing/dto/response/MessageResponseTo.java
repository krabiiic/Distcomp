package com.dedov.distributed_computing.dto.response;

public class MessageResponseTo {

    private long id;
    private long newsId;
    private String content;


    public long getnewsId() {
        return newsId;
    }
    public void setnewsId(long newsId) {
        this.newsId = newsId;
    }


    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
