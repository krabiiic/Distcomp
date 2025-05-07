package com.example.rest.dto;

public class InTopicDTO {
    private String method;
    private PostRequestTo postRequestTo;
    private String status;

    public InTopicDTO(String method, PostRequestTo postRequestTo, String status) {
        this.method = method;
        this.postRequestTo = postRequestTo;
        this.status = status;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public PostRequestTo getPostRequestTo() {
        return postRequestTo;
    }

    public void setPostRequestTo(PostRequestTo postRequestTo) {
        this.postRequestTo = postRequestTo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public InTopicDTO() {
    }
}
