package org.example.Impl.DTO;

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
