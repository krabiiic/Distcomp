package com.example.restservice.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Message implements Identifiable {
    private Long id;
    private Long articleId;
    private String content;
}
