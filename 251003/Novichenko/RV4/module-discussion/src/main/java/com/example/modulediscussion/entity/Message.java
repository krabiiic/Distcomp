package com.example.modulediscussion.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "tbl_message")
public class Message {
    @Id
    private int id;
    private int tweetId;
    private String content;
    private States state;
}
