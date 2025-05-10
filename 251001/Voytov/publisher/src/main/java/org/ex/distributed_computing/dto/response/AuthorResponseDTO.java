package org.ex.distributed_computing.dto.response;

import java.io.Serializable;

public record AuthorResponseDTO(
    Long id,
    String login,
    String password,
    String firstname,
    String lastname
) implements Serializable {

}
