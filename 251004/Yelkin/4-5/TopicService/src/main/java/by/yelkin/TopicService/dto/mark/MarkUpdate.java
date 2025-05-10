package by.yelkin.TopicService.dto.mark;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MarkUpdate {
    private Long id;
    @Size(min = 2, max = 32, message = "Name must be between 2 and 32 characters")
    private String name;
}
