package com.example.restservice.dto.kafka;

import com.example.restservice.model.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageEvent {
    private Long id;
    private Long articleId;
    private String content;
    private Message.MessageState state;
    private LocalDateTime created;
    private String type;
}