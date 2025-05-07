package by.zdanovich.Publisher.DTOs.Responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WriterResponseDTO implements Serializable {
    private long id;
    private String login;
    private String password;
    private String firstname;
    private String lastname;
}
