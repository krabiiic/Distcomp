package discussion.models;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

@PrimaryKeyClass
public class PostKey {
    @PrimaryKeyColumn(name = "country", type = PrimaryKeyType.PARTITIONED)
    private String country;

    @PrimaryKeyColumn(name = "issueId", type = PrimaryKeyType.CLUSTERED)
    private Long issueId;

    @PrimaryKeyColumn(name = "id", type = PrimaryKeyType.CLUSTERED)
    private Long id;

    public PostKey(String country, Long issueId, Long id) {
        this.country = country;
        this.issueId = issueId;
        this.id = id;
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

    public void setIssueId(Long issueId) {
        this.issueId = issueId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
