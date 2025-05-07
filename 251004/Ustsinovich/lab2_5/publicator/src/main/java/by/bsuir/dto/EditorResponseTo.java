package by.bsuir.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditorResponseTo {
    private Long id;
    private String firstname;
    private String lastname;
    private String login;
}
