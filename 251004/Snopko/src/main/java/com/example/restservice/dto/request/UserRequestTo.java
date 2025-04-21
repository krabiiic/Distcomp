package com.example.restservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRequestTo {
    @NotBlank(message = "Login cannot be blank")
    @Size(min = 2, max = 64, message = "Login must be between 2 and 64 characters")
    private String login;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, max = 128, message = "Password must be between 8 and 128 characters")
    private String password;

    @Size(min = 2, max = 64, message = "First name must be less than 64 characters")
    private String firstname;

    @Size(min = 2, max = 64, message = "Last name must be less than 64 characters")
    private String lastname;

    private Long id;

}
