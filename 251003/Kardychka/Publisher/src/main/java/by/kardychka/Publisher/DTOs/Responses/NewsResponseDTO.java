package by.kardychka.Publisher.DTOs.Responses;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class NewsResponseDTO {
    private long id;
    private Long creatorId;
    private String title;
    private String content;
    private Date created;
    private Date modified;
    private List<String> stickers;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public List<String> getStickers() {
        return stickers;
    }

    public void setStickers(List<String> stickers) {
        this.stickers = stickers;
    }
}
