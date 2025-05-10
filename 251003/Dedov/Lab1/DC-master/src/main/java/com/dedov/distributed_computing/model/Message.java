package com.dedov.distributed_computing.model;


public class Message {

    private long id;
    private long newsId;
    private String content;


    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }


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

}
