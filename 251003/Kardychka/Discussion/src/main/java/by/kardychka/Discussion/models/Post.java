package by.kardychka.Discussion.models;


import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.*;

import java.io.Serializable;


@Table("tbl_post")
public class Post implements Serializable {

    @PrimaryKeyClass
    public static class PostKey implements Serializable {
        @PrimaryKeyColumn(name = "country", type = PrimaryKeyType.PARTITIONED)
        private String country;

        @PrimaryKeyColumn(name = "id", ordinal = 0, type = PrimaryKeyType.CLUSTERED)
        private Long id;

        @PrimaryKeyColumn(name = "newsId", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
        private Long newsId;


        public PostKey() {
        }

        public PostKey(String country, Long newsId, Long id) {
            this.country = country;
            this.newsId = newsId;
            this.id = id;
        }

        public PostKey(Long newsId) {
            this.newsId = newsId;
        }

        public PostKey(String country, Long newsId) {
            this.country = country;
            this.newsId = newsId;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public Long getNewsId() {
            return newsId;
        }

        public void setNewsId(Long newsId) {
            this.newsId = newsId;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

    }


    @Column("content")
    private String content;

    @PrimaryKey
    private PostKey key;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public PostKey getKey() {
        return key;
    }

    public void setKey(PostKey key) {
        this.key = key;
    }
}