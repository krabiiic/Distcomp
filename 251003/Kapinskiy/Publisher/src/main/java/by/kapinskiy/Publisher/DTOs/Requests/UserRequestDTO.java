package by.kapinskiy.Publisher.DTOs.Requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserRequestDTO {
    private Long id;

    @NotBlank(message = "Login may not be blank")
    @Size(min = 2, max = 64, message = "Login should be between 2 and 64 symbols")
    private String login;

    @NotBlank(message = "Password may not be blank")
    @Size(min = 8, max = 128, message = "Password should be between 8 and 128 symbols")
    private String password;

    @NotBlank(message = "Firstname may not be blank")
    @Size(min = 2, max = 64, message = "Firstname should be between 2 and 64 symbols")
    private String firstname;

    @NotBlank(message = "Lastname may not be blank")
    @Size(min = 2, max = 64, message = "Lastname should be between 2 and 64 symbols")
    private String lastname;
}
