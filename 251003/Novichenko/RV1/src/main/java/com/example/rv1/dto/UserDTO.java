package com.example.rv1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class UserDTO {
    private int id;

    @JsonProperty("login")
    @Size(min = 2, max = 64, message = "Login must be between 2 and 64 characters")
    private String login;

    @JsonProperty("password")
    @Size(min = 2, max = 64, message = "Login must be between 2 and 64 characters")
    private String password;

    @JsonProperty("firstname")
    @Size(min = 2, max = 64, message = "Login must be between 2 and 64 characters")
    private String firstName;

    @JsonProperty("lastname")
    @Size(min = 2, max = 64, message = "Login must be between 2 and 64 characters")
    private String lastName;
}
