package com.dedov.distributed_computing.dto.request;

import com.dedov.distributed_computing.validation.groups.OnCreateOrUpdate;
import com.dedov.distributed_computing.validation.groups.OnPatch;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class MessageRequestTo {

    private long id;

    private long newsId;

    @Size(min = 2, max = 2048, message = "Content size must be between 2..64 characters", groups = {OnPatch.class, OnCreateOrUpdate.class})
    @NotBlank(message="Content can't be empty", groups = OnCreateOrUpdate.class)
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
