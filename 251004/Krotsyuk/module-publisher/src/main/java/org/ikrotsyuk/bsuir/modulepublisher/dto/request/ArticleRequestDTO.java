package org.ikrotsyuk.bsuir.modulepublisher.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleRequestDTO {
    @Min(0)
    private Long writerId;
    @NotBlank
    @Size(min = 2, max = 64)
    private String title;
    @NotBlank
    @Size(min = 4, max = 2048)
    private String content;
    private List<StickerRequestDTO> stickers = new LinkedList<>();
}
