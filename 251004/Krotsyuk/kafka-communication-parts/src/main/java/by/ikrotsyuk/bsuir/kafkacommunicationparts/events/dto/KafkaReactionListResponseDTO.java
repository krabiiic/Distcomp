package by.ikrotsyuk.bsuir.kafkacommunicationparts.events.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KafkaReactionListResponseDTO {
    private String eventId;
    private List<KafkaReactionResponseEventDTO> reactionDTO;
}
