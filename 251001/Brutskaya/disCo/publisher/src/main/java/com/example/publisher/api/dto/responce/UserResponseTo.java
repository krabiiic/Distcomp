package com.example.publisher.api.dto.responce;

import lombok.Data;

@Data
public class UserResponseTo {

    private Long id;
    private String login;
    private String password;
    private String firstname;
    private String lastname;
}
