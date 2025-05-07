package by.andrewbesedin.distcomp.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ReactionRequestTo {
    long id;
    long articleId;
    @Size(min = 2, max = 32)
    String content;
}
