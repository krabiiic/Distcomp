package com.lab.labDCP.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NoticeRequestTo {
    private Long id;
    private String content;
    private Long storyId;
}