package by.zdanovich.Publisher.DTOs.Responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MessageResponseDTO implements Serializable {
    private long id;
    private Long issueId;
    private String content;
}
