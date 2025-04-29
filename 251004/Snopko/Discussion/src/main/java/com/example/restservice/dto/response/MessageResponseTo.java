package com.example.restservice.dto.response;

import lombok.Data;


@Data
public class MessageResponseTo {
    private Long id;
    private Long articleId;
    private String content;
}
