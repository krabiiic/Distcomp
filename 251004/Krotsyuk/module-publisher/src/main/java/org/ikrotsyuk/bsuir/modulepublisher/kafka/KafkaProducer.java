package org.ikrotsyuk.bsuir.modulepublisher.kafka;

import by.ikrotsyuk.bsuir.kafkacommunicationparts.events.dto.KafkaExceptionDTO;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.ikrotsyuk.bsuir.modulepublisher.exception.exceptions.NotFoundException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Component
public class KafkaProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final KafkaConsumer kafkaConsumer;

    @SneakyThrows
    public Object sendRequest(Object requestDTO, String eventId){
        CompletableFuture<Object> future = kafkaConsumer.waitForResponse(eventId);

        kafkaTemplate.send(KafkaConstants.IN_TOPIC_NAME, eventId, requestDTO);

        Object response = future.get();
        if(response.getClass() == KafkaExceptionDTO.class)
            throw new NotFoundException();
        return response;
    }
}
