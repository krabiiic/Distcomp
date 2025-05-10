package com.example.JPA2.Impl.DTO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserResponseTo {
    private long id;
    private String login;
    private String password;
    private String firstname;
    private String lastname;
}
