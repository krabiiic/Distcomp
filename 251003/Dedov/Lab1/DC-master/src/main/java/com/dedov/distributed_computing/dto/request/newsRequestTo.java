package com.dedov.distributed_computing.dto.request;

import com.dedov.distributed_computing.validation.groups.OnCreateOrUpdate;
import com.dedov.distributed_computing.validation.groups.OnPatch;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class newsRequestTo {

    private long id;

    private long editorId;

    @Size(min = 2, max = 64, message = "Title size must be between 2..64 characters", groups = {OnPatch.class, OnCreateOrUpdate.class})
    @NotBlank(message="Title can't be empty", groups = OnCreateOrUpdate.class)
    private String title;

    @Size(min = 4, max = 2048, message = "Content size must be between 2..64 characters", groups = {OnPatch.class, OnCreateOrUpdate.class})
    @NotBlank(message="Content can't be empty", groups = OnCreateOrUpdate.class)
    private String content;


    public long geteditorId() {
        return editorId;
    }
    public void seteditorId(long editorId) {
        this.editorId = editorId;
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


    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
}
