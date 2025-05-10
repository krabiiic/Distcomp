package by.zdanovich.Publisher.DTOs.Requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MessageRequestDTO {
    private Long id;

    @NotNull(message = "Issue id can't be null")
    private Long issueId;

    @NotBlank(message = "Content may not be blank")
    @Size(min = 2, max = 2048, message = "Content should be between 2 and 2048 symbols")
    private String content;

    public MessageRequestDTO(Long id) {
        this.id = id;
    }
}
