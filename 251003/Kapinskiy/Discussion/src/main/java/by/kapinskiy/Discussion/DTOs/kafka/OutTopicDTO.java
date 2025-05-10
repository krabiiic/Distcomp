package by.kapinskiy.Discussion.DTOs.kafka;


import by.kapinskiy.Discussion.DTOs.Responses.NoteResponseDTO;
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
    private NoteResponseDTO noteResponseDTO;

    private List<NoteResponseDTO> noteResponsesListDTO;

    private String status;

    private String error;


    public OutTopicDTO(NoteResponseDTO noteResponseDTO, String status) {
        this.noteResponseDTO = noteResponseDTO;
        this.status = status;
    }

    public OutTopicDTO(String error, String status) {
        this.status = status;
        this.error = error;
    }

    public OutTopicDTO(List<NoteResponseDTO> noteResponsesListDTO, String status) {
        this.status = status;
        this.noteResponsesListDTO = noteResponsesListDTO;
    }

}
