package com.rita.publisher.dto;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record StickerUpdateDTO(@NotNull Long id,@NotNull @Length(min = 2, max = 32) String name) {
}
