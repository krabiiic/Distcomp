package com.example.model;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import static org.springframework.data.cassandra.core.cql.PrimaryKeyType.CLUSTERED;
import static org.springframework.data.cassandra.core.cql.PrimaryKeyType.PARTITIONED;

@Table("tbl_reaction")
public class Reaction {

    public enum State {
        PENDING,
        APPROVE,
        DECLINE
    }

    @PrimaryKeyClass
    public static class ReactionKey {
        @PrimaryKeyColumn(name = "country", ordinal = 0, type = PARTITIONED)
        private String country = "default";

        @PrimaryKeyColumn(name = "issue_id", ordinal = 1, type = CLUSTERED)
        private Long issueId;

        @PrimaryKeyColumn(name = "id", ordinal = 2, type = CLUSTERED)
        private Long id;

        public ReactionKey() {}

        public ReactionKey(String country, Long issueId, Long id) {
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

    @org.springframework.data.cassandra.core.mapping.PrimaryKey
    private ReactionKey key;

    @Column("content")
    private String content;

    @Column("state")
    private State state;

    public Reaction() {}

    public Reaction(ReactionKey key, String content) {
        this.key = key;
        this.content = content;
        this.state = State.PENDING;
    }

    public ReactionKey getKey() {
        return key;
    }
    public void setKey(ReactionKey key) {
        this.key = key;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public State getState() {
        return state;
    }
    public void setState(State state) {
        this.state = state;
    }
}