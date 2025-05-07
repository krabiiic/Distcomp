package by.ikrotsyuk.bsuir.kafkacommunicationparts.events;

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
public class KafkaGetReactionEvent {
    private String eventId;
    private Long reactionId;
}
