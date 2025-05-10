package lab1.modelDTOs;


import java.util.Date;

public class IssueResponseTo {
    private long Id;
    private long UserId;
    private String Title = "";
    private String Content = "";
    private Date Created;
    private Date Modified;

    public IssueResponseTo() {
    }

    public IssueResponseTo(long id, long userId, String title, String content, Date created, Date modified) {
        Id = id;
        UserId = userId;
        Title = title;
        Content = content;
        Created = created;
        Modified = modified;
    }

    public Date getCreated() {
        return Created;
    }

    public void setCreated(Date created) {
        Created = created;
    }

    public Date getModified() {
        return Modified;
    }

    public void setModified(Date modified) {
        Modified = modified;
    }

    public long getUserId() {
        return UserId;
    }

    public void setUserId(long userId) {
        UserId = userId;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }
    
    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }
}
