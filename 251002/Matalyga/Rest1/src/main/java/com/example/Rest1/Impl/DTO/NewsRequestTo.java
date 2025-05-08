package com.example.Rest1.Impl.DTO;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Setter
@Getter
public class NewsRequestTo {
    @NotNull
    private long id;
    private long userId;
    @NotBlank
    @Size(min = 2, max = 64)
    private String title;
    @Size(min = 4, max = 2048)
    private String content;
    private Date created;
    private Date modified;
}

