package com.lab.labDCP.dto;

import com.lab.labDCP.entity.NoticeState;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NoticeRequestKafkaTo {
    private Object object;
    private NoticeState noticeState;
}
