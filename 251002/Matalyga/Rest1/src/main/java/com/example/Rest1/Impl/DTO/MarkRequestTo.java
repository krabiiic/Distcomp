package com.example.Rest1.Impl.DTO;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
public class MarkRequestTo {
    @NotNull
    private long id;
    @NotBlank
    @Size(min = 2, max = 32)
    private String name;
}
