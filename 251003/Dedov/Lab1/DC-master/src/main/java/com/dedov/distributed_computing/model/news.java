package com.dedov.distributed_computing.model;

import java.util.Date;

public class news {

    private long id;
    private long editorId;
    private String title;
    private String content;
    private Date created;
    private Date modified;


    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }


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

    public Date getCreated() {
        return created;
    }
    public void setCreated(Date created) {
        this.created = created;
    }


    public Date getModified() {
        return modified;
    }
    public void setModified(Date modified) {
        this.modified = modified;
    }
}
