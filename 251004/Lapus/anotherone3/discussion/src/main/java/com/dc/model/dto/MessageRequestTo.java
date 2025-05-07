package com.dc.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class MessageRequestTo {
    private Long id;
    private long newsId;
    private String content;

    public MessageRequestTo(long newsId, String content) {
        this.newsId = newsId;
        this.content = content;
    }
}
