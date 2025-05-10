package org.ex.distributed_computing.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import javax.swing.text.html.parser.Entity;
import org.ex.distributed_computing.model.EntityStatus;

public record ReactionRequestDTO(

    Long id,

    @NotNull
    Long tweetId,

    @NotNull
    @Size(min = 2, max = 2048)
    String content,

    EntityStatus status,

    String country
) implements Serializable {

}

