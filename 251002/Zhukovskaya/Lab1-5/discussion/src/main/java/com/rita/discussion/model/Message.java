package com.rita.discussion.model;

import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("tbl_message")
public class Message{
    @PrimaryKey
    private Long id;
    private Long tweetId;
    private String content;
//    @Transient
//    private State state;

}
