package by.ikrotsyuk.bsuir.kafkacommunicationparts.events;

import by.ikrotsyuk.bsuir.kafkacommunicationparts.events.dto.ReactionDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KafkaCreateReactionEvent {
    private String eventId;
    private ReactionDTO data;
}
