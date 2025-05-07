package com.example.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class PostUpdate {
    @NotNull(message = "ID should not be null.")
    @Positive(message = "ID should be a positive number.")
    private Long id;

    @NotNull(message = "topicId should not be null.")
    @Positive(message = "topicId should be a positive number.")
    private Long topicId;

    @NotBlank(message = "Content should not be blank.")
    @Size(min = 2, max = 2048, message = "Content size should be between 2 and 2048 chars.")
    private String content;

    public @NotNull(message = "ID should not be null.") @Positive(message = "ID should be a positive number.") Long getId() {
        return id;
    }

    public void setId(@NotNull(message = "ID should not be null.") @Positive(message = "ID should be a positive number.") Long id) {
        this.id = id;
    }

    public @NotNull(message = "topicId should not be null.") @Positive(message = "topicId should be a positive number.") Long getTopicId() {
        return topicId;
    }

    public void setTopicId(@NotNull(message = "topicId should not be null.") @Positive(message = "topicId should be a positive number.") Long topicId) {
        this.topicId = topicId;
    }

    public @NotBlank(message = "Content should not be blank.") @Size(min = 2, max = 2048, message = "Content size should be between 2 and 2048 chars.") String getContent() {
        return content;
    }

    public void setContent(@NotBlank(message = "Content should not be blank.") @Size(min = 2, max = 2048, message = "Content size should be between 2 and 2048 chars.") String content) {
        this.content = content;
    }
}