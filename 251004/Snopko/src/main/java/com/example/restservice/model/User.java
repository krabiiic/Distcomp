package com.example.restservice.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class User implements Identifiable{
    private Long id;
    private String login;
    private String password;
    private String firstName;
    private String lastName;

}
