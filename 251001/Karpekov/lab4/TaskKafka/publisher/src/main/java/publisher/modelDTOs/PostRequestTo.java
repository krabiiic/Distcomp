package publisher.modelDTOs;


public class PostRequestTo {
    private long id;
    private long issueId;
    private String content;

    public PostRequestTo() {
    }

    public PostRequestTo(long id, long issueId, String content) {
        this.id = id;
        this.issueId = issueId;
        this.content = content;
    }

    public long getIssueId() {
        return issueId;
    }

    public void setIssueId(long issueId) {
        this.issueId = issueId;
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
