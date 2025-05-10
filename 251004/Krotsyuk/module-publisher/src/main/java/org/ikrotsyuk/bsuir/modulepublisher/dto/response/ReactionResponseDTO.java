package org.ikrotsyuk.bsuir.modulepublisher.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReactionResponseDTO implements Serializable {
    @NotNull
    private Long id;
    @NotNull
    private Long articleId;
    @NotBlank
    private String country;
    @NotBlank
    @Size(min = 2, max = 2048)
    private String content;
}
