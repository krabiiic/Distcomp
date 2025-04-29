package com.example.restservice.dto.response;

import lombok.Data;

@Data
public class UserResponseTo {
    private Long id;
    private String login;
    private String password;
    private String firstname;
    private String lastname;
}
