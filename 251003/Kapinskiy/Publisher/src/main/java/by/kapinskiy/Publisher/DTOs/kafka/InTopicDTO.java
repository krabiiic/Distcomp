package by.kapinskiy.Publisher.DTOs.kafka;

import by.kapinskiy.Publisher.DTOs.Requests.NoteRequestDTO;

public record InTopicDTO(
        String method,
        NoteRequestDTO noteRequestDTO,
        String status
) {

}
