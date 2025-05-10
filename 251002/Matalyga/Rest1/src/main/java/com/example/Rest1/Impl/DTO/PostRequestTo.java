package com.example.Rest1.Impl.DTO;

import lombok.Getter;
import lombok.Setter;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Setter
@Getter
public class PostRequestTo {
    @NotNull
    private long id;
    @Size(min = 2, max = 2048)
    private String content;
    private  long newsId;
}
