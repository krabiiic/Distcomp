package com.example.restservice.dto.request;

import com.example.restservice.model.Mark;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class ArticleRequestTo {
    @NotBlank(message = "Title cannot be blank")
    @Size(min = 2, max = 64, message = "Title must be between 2 and 64 characters")
    private String title;

    @NotBlank(message = "Content cannot be blank")
    @Size(min = 4, max = 2048, message = "Content must be between 4 and 2048 characters")
    private String content;


    private LocalDateTime created;
    private LocalDateTime modified;

    private Long id;
    private Long userId;

    private List<String> marks = new ArrayList<>();
}
