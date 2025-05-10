package org.ikrotsyuk.bsuir.modulepublisher.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReactionRequestDTO {
    @Min(0)
    private Long articleId;
    private String country;
    @NotBlank
    @Size(min = 2, max = 2048)
    private String content;
}
