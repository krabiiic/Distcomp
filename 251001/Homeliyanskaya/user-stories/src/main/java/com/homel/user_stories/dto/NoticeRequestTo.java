package com.homel.user_stories.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class NoticeRequestTo {
    private Long id;

    @NotNull
    private Long storyId;

    @NotBlank
    @Size(min = 2, max = 2048)
    private String content;
}
