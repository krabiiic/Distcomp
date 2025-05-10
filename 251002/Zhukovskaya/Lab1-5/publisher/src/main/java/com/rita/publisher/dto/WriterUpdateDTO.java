package com.rita.publisher.dto;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record WriterUpdateDTO (@NotNull Long id,
                               @NotNull(groups = UpdateGroup.class) @Length(min = 2, max = 64,groups = UpdateGroup.class) String login,
                               @NotNull(groups = UpdateGroup.class) @Length(min = 8, max = 128,groups = UpdateGroup.class)  String password,
                               @NotNull(groups = UpdateGroup.class) @Length(min = 2, max = 64,groups = UpdateGroup.class) String firstname,
                               @NotNull(groups = UpdateGroup.class) @Length(min = 2, max = 64,groups = UpdateGroup.class) String lastname){
}
