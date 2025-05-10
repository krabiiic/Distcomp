package lab1.modelDTOs;


import java.util.Date;

public class PostResponseTo {
    private long Id;
    private long IssueId;
    private String Content = "";

    public PostResponseTo() {
    }

    public PostResponseTo(long id, long issueId, String content) {
        Id = id;
        IssueId = issueId;
        Content = content;
      
    }

    public long getIssueId() {
        return IssueId;
    }

    public void setIssueId(long issueId) {
        IssueId = issueId;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }
}
