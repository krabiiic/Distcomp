package com.example.modulepublisher.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("Message")
public class MessageDTO implements Serializable  {
    private int id;
    @JsonProperty("tweetId")
    private int tweetId;
    @JsonProperty("content")
    @Size(min = 2, max = 64, message = "Login must be between 2 and 64 characters")
    private String content;
    private String action;
}
