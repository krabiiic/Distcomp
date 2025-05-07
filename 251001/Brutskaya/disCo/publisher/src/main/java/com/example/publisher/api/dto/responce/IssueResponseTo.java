package com.example.publisher.api.dto.responce;

import lombok.Data;

import java.time.Instant;

@Data
public class IssueResponseTo {

    private Long id;
    private Long userId;
    private String title;
    private String content;
    private Instant created;
    private Instant modified;
}
