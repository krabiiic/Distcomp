package com.lab.labDC.entity;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NoticeKafka {
    private Object object;
    private NoticeState noticeState;
}