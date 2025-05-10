package org.ikrotsyuk.bsuir.modulepublisher.dto.response;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StickerResponseDTO implements Serializable {
    @Min(0)
    private Long id;
    @NotBlank
    @Size(min = 2, max = 32)
    private String name;
}

