package com.example.JPA2.Impl.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PostRequestTo {
    @NotNull
    private long id;
    @Size(min = 2, max = 2048)
    private String content;
    @NotNull
    private long newsId;
}
