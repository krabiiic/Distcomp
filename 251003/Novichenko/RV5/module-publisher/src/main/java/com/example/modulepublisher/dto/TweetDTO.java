package com.example.modulepublisher.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@RedisHash("Tweet")
public class TweetDTO implements Serializable {
    private int id;
    @JsonProperty("userId")
    private int userId;
    @JsonProperty("title")
    @Size(min = 2, max = 64, message = "Login must be between 2 and 64 characters")
    private String title;
    @JsonProperty("content")
    @Size(min = 4, max = 64, message = "Login must be between 2 and 64 characters")
    private String content;
}
