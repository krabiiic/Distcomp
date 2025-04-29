package com.example.restservice.model;

import lombok.Data;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

//CREATE KEYSPACE IF NOT EXISTS distcomp
//WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};

@Table("tbl_message")
@Data
public class Message{
    @PrimaryKey
    private Long id;

    @Column
    private Long articleId;

    @Column
    private String content;
}
