package com.example.modulepublisher.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@RedisHash("User")
public class UserDTO implements Serializable {
    private int id;

    @JsonProperty("login")
    @Size(min = 2, max = 64, message = "Login must be between 2 and 64 characters")
    private String login;

    @JsonProperty("password")
    @Size(min = 8, max = 64, message = "Login must be between 2 and 64 characters")
    private String password;

    @JsonProperty("firstname")
    @Size(min = 2, max = 64, message = "Login must be between 2 and 64 characters")
    private String firstName;

    @JsonProperty("lastname")
    @Size(min = 2, max = 64, message = "Login must be between 2 and 64 characters")
    private String lastName;
}
