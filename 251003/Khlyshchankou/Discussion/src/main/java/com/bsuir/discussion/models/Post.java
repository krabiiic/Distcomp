package com.bsuir.discussion.models;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.*;

import java.io.Serializable;


@Table("tbl_post")
public class Post {

    @PrimaryKeyClass
    public static class PostKey implements Serializable {
        @PrimaryKeyColumn(name = "country", type = PrimaryKeyType.PARTITIONED)
        private String country = "Republic Of Belarus";

        @PrimaryKeyColumn(name = "id", ordinal = 0, type = PrimaryKeyType.CLUSTERED)
        private Long id;

        @PrimaryKeyColumn(name = "topicId", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
        private Long topicId;

        public PostKey() {}

        public PostKey(String country, Long topicId, Long id) {
            this.country = country;
            this.topicId = topicId;
            this.id = id;
        }

        public PostKey(Long topicId) { this.topicId = topicId; }

        public String getCountry() { return country; }
        public void setCountry(String country) { this.country = country; }

        public Long getTopicId() { return topicId; }
        public void setTopicId(Long topicId) { this.topicId = topicId; }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
    }


    @Column("content")
    private String content;

    @PrimaryKey
    private PostKey key;

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public PostKey getKey() { return key; }
    public void setKey(PostKey key) { this.key = key; }
}
