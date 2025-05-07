package by.ikrotsyuk.bsuir.kafkacommunicationparts.events.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KafkaReactionResponseDTO {
    private String eventId;
    private KafkaReactionResponseEventDTO reactionDTO;
}
