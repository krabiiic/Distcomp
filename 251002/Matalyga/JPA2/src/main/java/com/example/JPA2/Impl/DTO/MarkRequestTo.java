package com.example.JPA2.Impl.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MarkRequestTo {
    @NotNull
    private long id;
    @NotBlank
    @Size(min = 2, max = 32)
    private String name;
}
