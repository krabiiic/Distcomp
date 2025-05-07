package com.lab.labDC.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NoticeResponseTo {
    private Long id;
    private String content;
    private Long storyId;
}