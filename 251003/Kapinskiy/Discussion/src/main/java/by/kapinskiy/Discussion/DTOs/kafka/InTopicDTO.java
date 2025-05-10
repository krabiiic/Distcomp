package by.kapinskiy.Discussion.DTOs.kafka;


import by.kapinskiy.Discussion.DTOs.Requests.NoteRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InTopicDTO {
    private String method;

    private NoteRequestDTO noteRequestDTO;

    private String status;
}
