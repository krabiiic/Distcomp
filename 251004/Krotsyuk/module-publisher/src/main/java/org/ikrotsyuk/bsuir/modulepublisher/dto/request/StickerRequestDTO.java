package org.ikrotsyuk.bsuir.modulepublisher.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StickerRequestDTO {
    @NotBlank
    @Size(min = 2, max = 32)
    private String name;
}
