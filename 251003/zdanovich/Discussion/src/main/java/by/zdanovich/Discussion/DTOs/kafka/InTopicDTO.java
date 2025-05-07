package by.zdanovich.Discussion.DTOs.kafka;


import by.zdanovich.Discussion.DTOs.Requests.MessageRequestDTO;
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

    private MessageRequestDTO messageRequestDTO;

    private String status;
}
