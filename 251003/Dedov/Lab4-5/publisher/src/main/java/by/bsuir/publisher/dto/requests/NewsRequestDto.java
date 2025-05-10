package by.bsuir.publisher.dto.requests;

import by.bsuir.publisher.dto.requests.BaseRequestDto;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class NewsRequestDto extends BaseRequestDto {
    private Long editorId;

    @Size(min = 3, max = 32)
    private String title;

    @Size(min = 3, max = 32)
    @Pattern(regexp="^.*[a-zA-Z]+.*$")
    private String content;
}
