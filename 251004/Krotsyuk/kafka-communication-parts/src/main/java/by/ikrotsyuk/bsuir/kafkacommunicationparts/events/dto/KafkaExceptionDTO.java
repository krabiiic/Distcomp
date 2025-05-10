package by.ikrotsyuk.bsuir.kafkacommunicationparts.events.dto;

import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
public class KafkaExceptionDTO {
    private String message;
    private OffsetDateTime offsetDateTime = OffsetDateTime.now();

    public KafkaExceptionDTO(String message) {
        this.message = message;
    }
}
