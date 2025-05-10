package by.ikrotsyuk.bsuir.modulediscussion.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class KafkaProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendResponse(Object responseDTO, String eventId){
        kafkaTemplate.send(KafkaConstants.OUT_TOPIC_NAME, eventId, responseDTO);
    }
}
