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
public class TagRequestDTO {

    private Long id;

    private Long issueId;

    @NotBlank(message = "Name may not be blank")
    @Size(min = 2, max = 32, message = "Name should be between 2 and 32 symbols")
    private String name;
}
