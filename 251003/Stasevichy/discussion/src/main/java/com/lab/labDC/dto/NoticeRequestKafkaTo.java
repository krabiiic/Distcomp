package com.lab.labDC.dto;

import com.lab.labDC.entity.NoticeState;
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
