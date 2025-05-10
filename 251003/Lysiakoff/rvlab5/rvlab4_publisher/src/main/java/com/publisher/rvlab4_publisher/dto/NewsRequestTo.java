package com.publisher.rvlab4_publisher.dto;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NewsRequestTo {
    private Long id;
    private String title;
    private String content;
    private Long authorId;
    private List<UUID> markIds;
    private List<String> marks;
    private List<UUID> messageIds;

}