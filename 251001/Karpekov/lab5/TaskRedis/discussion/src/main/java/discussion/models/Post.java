package discussion.models;

import discussion.enums.PostState;
import org.springframework.data.annotation.Transient;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.*;


@Table("tbl_post")
public class Post {
    @PrimaryKey
    private PostKey id;

    @Column
    private String content;

    public Post() {
    }

    public Post(PostKey id, String content) {
        this.id = id;
        this.content = content;
    }

    public PostKey getId() {
        return id;
    }

    public void setId(PostKey id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
