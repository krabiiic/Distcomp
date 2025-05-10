package com.example.JPA2.Impl.DTO;

import com.example.JPA2.Impl.Entity.User;
import lombok.Getter;
import lombok.Setter;
import java.sql.Timestamp;

@Setter
@Getter
public class NewsResponseTo {
    private long id;
    private long userId;
    private String title;
    private String content;
    private Timestamp created;
    private Timestamp modified;
}
