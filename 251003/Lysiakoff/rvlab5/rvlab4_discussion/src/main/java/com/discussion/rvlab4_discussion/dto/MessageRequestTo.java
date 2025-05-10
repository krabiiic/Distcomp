package com.discussion.rvlab4_discussion.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MessageRequestTo {
    private Long id;
    private String content;
    private Long newsId;
}