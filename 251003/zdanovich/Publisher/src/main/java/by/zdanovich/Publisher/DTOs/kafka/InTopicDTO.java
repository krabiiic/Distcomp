package by.zdanovich.Publisher.DTOs.kafka;

import by.zdanovich.Publisher.DTOs.Requests.MessageRequestDTO;

public record InTopicDTO(
        String method,
        MessageRequestDTO messageRequestDTO,
        String status
) {

}
