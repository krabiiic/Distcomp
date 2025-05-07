package com.example.rest.dto;

import java.util.List;

public class OutTopicDTO {
    private PostResponseTo postResponseTo;
    private List<PostResponseTo> postResponseTos;
    private String status;
    private String error;

    public PostResponseTo getPostResponseTo() {
        return postResponseTo;
    }

    public void setPostResponseTo(PostResponseTo postResponseTo) {
        this.postResponseTo = postResponseTo;
    }

    public List<PostResponseTo> getPostResponseTos() {
        return postResponseTos;
    }

    public void setPostResponseTos(List<PostResponseTo> postResponseTos) {
        this.postResponseTos = postResponseTos;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public OutTopicDTO(PostResponseTo postResponseTo, String status) {
        this.postResponseTo = postResponseTo;
        this.status = status;
    }

    public OutTopicDTO(List<PostResponseTo> postResponseTos, String status) {
        this.postResponseTos = postResponseTos;
        this.status = status;
    }

    public OutTopicDTO(String s, String decline) {
    }

    public OutTopicDTO(PostResponseTo postResponseTo, List<PostResponseTo> postResponseTos, String status, String error) {
        this.postResponseTo = postResponseTo;
        this.postResponseTos = postResponseTos;
        this.status = status;
        this.error = error;
    }

    public OutTopicDTO(PostResponseTo postResponseTo) {
        this.postResponseTo = postResponseTo;
    }

    public OutTopicDTO(List<PostResponseTo> postResponseTos) {
        this.postResponseTos = postResponseTos;
    }

    public OutTopicDTO(String status) {
        this.status = status;
    }
}