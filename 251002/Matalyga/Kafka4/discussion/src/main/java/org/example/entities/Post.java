package org.example.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import static org.springframework.data.cassandra.core.cql.PrimaryKeyType.CLUSTERED;
import static org.springframework.data.cassandra.core.cql.PrimaryKeyType.PARTITIONED;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(value = "tbl_post")
public class Post {

    @PrimaryKeyColumn(name = "country", type = PARTITIONED)
    private String country;

    @PrimaryKeyColumn(name = "newsid", ordinal = 0, type = CLUSTERED)
    private Long newsId;

    @PrimaryKeyColumn(name = "id", ordinal = 1, type = CLUSTERED)
    private Long id;

    private String content;
}