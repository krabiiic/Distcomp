package com.rita.publisher.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;

public record WriterCreateDTO(@NotNull  @Size(min = 8, max = 64) String login, @NotNull @Length(min = 8, max = 128) String password, @NotNull @Length(min = 2, max = 64) String firstname, @NotNull @Length(min = 2, max = 64) String lastname) {
}
