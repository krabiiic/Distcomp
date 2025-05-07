package com.example.discussion.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.io.Serializable;

@PrimaryKeyClass
@Data
public class ReactionKey implements Serializable {
    @PrimaryKeyColumn(name = "country", type = PrimaryKeyType.PARTITIONED)
    private String country;

    @PrimaryKeyColumn(name = "newsId", type = PrimaryKeyType.CLUSTERED)
    private Long topicId;

    @PrimaryKeyColumn(name = "id", type = PrimaryKeyType.CLUSTERED)
    private Long id;

    public ReactionKey(String country, Long topicId, Long id) {
        this.country = country;
        this.topicId = topicId;
        this.id = id;
    }

}
