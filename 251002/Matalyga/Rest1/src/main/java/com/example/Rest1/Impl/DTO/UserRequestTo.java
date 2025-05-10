package com.example.Rest1.Impl.DTO;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Setter
@Getter
public class UserRequestTo {
    @NotNull
    private long id;
    @NotBlank
    @Size(min = 2, max = 64)
    private String login;
    @Size(min = 8, max = 128)
    private String password;
    @Size(min = 2, max = 64)
    private String firstname;
    @Size(min = 2, max = 64)
    private String lastname;
}
