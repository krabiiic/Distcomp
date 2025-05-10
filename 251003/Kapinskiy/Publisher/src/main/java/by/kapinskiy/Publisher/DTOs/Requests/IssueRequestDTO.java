package by.kapinskiy.Publisher.DTOs.Requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class IssueRequestDTO {
    private Long id;

    @NotNull(message = "User id can't be null")
    private Long userId;

    @NotBlank(message = "Title may not be blank")
    @Size(min = 2, max = 64, message = "Title should be between 2 and 64 symbols")
    private String title;

    @NotBlank(message = "Content may not be blank")
    @Size(min = 4, max = 2048, message = "Content should be between 4 and 2048 symbols")
    private String content;

    private List<String> tags = new ArrayList<>();
}
