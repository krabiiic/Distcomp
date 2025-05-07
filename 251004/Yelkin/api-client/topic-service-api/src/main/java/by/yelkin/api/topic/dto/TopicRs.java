package by.yelkin.api.topic.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TopicRs {
    private Long id;
    private Long creatorId;
    private String title;
    private String content;
    private LocalDateTime created;
    private LocalDateTime modified;
}
