package by.zdanovich.Discussion.DTOs.kafka;


import by.zdanovich.Discussion.DTOs.Responses.MessageResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OutTopicDTO {
    private MessageResponseDTO messageResponseDTO;

    private List<MessageResponseDTO> messageResponseDTOList;

    private String status;

    private String error;


    public OutTopicDTO(MessageResponseDTO messageResponseDTO, String status) {
        this.messageResponseDTO = messageResponseDTO;
        this.status = status;
    }

    public OutTopicDTO(String error, String status) {
        this.status = status;
        this.error = error;
    }

    public OutTopicDTO(List<MessageResponseDTO> messageResponseDTOList, String status) {
        this.status = status;
        this.messageResponseDTOList = messageResponseDTOList;
    }

}
