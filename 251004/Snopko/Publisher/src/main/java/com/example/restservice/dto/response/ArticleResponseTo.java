package com.example.restservice.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ArticleResponseTo {
    private Long id;
    private Long userId;
    private String title;
    private String content;
    private LocalDateTime created;
    private LocalDateTime modified;
}
