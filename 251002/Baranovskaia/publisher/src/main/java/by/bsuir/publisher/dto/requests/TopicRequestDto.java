package by.bsuir.publisher.dto.requests;

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
public class TopicRequestDto extends BaseRequestDto {
    private Long userId;

    @Size(min = 2, max = 64)
    private String title;

    @Size(min = 4, max = 2048)
    @Pattern(regexp="^.*[a-zA-Z]+.*$")
    private String content;
}
