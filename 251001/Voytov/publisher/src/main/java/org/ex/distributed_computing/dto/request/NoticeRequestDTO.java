package org.ex.distributed_computing.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.ex.distributed_computing.model.EntityStatus;

public record NoticeRequestDTO(

    Long id,

    @NotNull
    Long newsId,

    @NotNull
    @Size(min = 2, max = 2048)
    String content,

    EntityStatus status,

    String country
) {

}

