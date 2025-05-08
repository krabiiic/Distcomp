package com.example.JPA2.Impl.DTO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PostResponseTo {
    private long id;
    private String content;
    private long newsId;
}
