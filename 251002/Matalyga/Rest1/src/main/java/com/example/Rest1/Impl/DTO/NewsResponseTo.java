package com.example.Rest1.Impl.DTO;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Setter
@Getter
public class NewsResponseTo {
    private long id;
    private long userId;
    private String title;
    private String content;
    private Date created;
    private Date modified;
}
