package com.example.rv1.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tweet {
    private int id;
    private int userId;
    private String title;
    private String content;
    private LocalDateTime created;
    private LocalDateTime modified;
}
