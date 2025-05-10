package com.dc.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewsDTO {

    private long id;

    private long authorId;

    private String title;

    private String content;

    private Timestamp created;

    private Timestamp modified;

}
