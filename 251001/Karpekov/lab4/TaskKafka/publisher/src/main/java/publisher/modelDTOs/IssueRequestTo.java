package publisher.modelDTOs;


import java.util.Date;
import java.util.Set;

public class IssueRequestTo {
    private long id;
    private long userId;
    private String title;
    private String content;
    private Date created;
    private Date modified;
    private Set<String> tags;

    public IssueRequestTo() {
    }

    public IssueRequestTo(long id, long userId, String title, String content, Date created, Date modified) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.created = created;
        this.modified = modified;
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

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }
}
