package com.publisher.rvlab4_publisher.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MessageResponseTo {
    private Long id;
    private String content;
    private Long newsId;
}