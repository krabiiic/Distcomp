package discussion.modelDTOs;


public class PostResponseTo {
    private String country;
    private Long id;
    private Long issueId;
    private String content;

    public PostResponseTo() {
    }

    public PostResponseTo(String country, long id, long issueId, String content) {
        this.country = country;
        this.id = id;
        this.issueId = issueId;
        this.content = content;
      
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Long getIssueId() {
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

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
