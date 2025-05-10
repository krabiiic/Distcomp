package com.example.rv1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MarkDTO {
    private int id;
    @JsonProperty("name")
    @Size(min = 2, max = 32, message = "Login must be between 2 and 64 characters")
    private String name;
}
