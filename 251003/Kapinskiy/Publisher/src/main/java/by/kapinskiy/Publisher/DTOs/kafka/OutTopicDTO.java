package by.kapinskiy.Publisher.DTOs.kafka;

import by.kapinskiy.Publisher.DTOs.Responses.NoteResponseDTO;
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
    private NoteResponseDTO noteResponseDTO;
    private List<NoteResponseDTO> noteResponsesListDTO;
    private String status;
    private String error;
}
