package com.example.JPA2.Impl.DTO;

import com.example.JPA2.Impl.Entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import java.sql.Timestamp;

@Setter
@Getter
public class NewsRequestTo {
    @NotNull
    private long id;
    @NotNull
    private long userId;
    @NotBlank
    @Size(min = 2, max = 64)
    private String title;
    @Size(min = 4, max = 2048)
    private String content;
    private Timestamp created;
    private Timestamp modified;
}

