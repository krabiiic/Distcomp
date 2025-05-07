package by.zdanovich.Publisher.DTOs.kafka;

import by.zdanovich.Publisher.DTOs.Responses.MessageResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OutTopicDTO {
    private MessageResponseDTO messageResponseDTO;

    private List<MessageResponseDTO> messageResponseDTOList;

    private String status;

    private String error;
}
