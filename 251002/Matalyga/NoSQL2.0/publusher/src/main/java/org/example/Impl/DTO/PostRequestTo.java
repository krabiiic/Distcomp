package org.example.Impl.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostRequestTo {
    private Long id;
    @Size(min = 2, max = 2048)
    private String content;
    private Long newsId;
}