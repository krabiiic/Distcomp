package lab1.modelDTOs;


public class PostRequestTo {
    private long Id;
    private long IssueId;
    private String Content = "";

    public PostRequestTo() {
    }

    public PostRequestTo(long id, long issueId, String content) {
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
