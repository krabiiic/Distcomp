package discussion.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import discussion.controllers.PostKafkaController;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.UUID;
import java.util.logging.Logger;

@EnableKafka
@Component
@RequiredArgsConstructor
public class KafkaServer {
    public static final String REQUEST_TOPIC = "InTopic";
    public static final String RESPONSE_TOPIC = "OutTopic";
    public final PostKafkaController postKafkaController;
    private final KafkaTemplate<String, String> sender;
    private final ObjectMapper mapper;

    @SneakyThrows
    @KafkaListener(topics = REQUEST_TOPIC, groupId = "groupId =#{T(java.util.UUID).randomUUID().toString()}")
    private void getMessage(ConsumerRecord<String, String> record){
        UUID uuid = UUID.fromString(record.key());

        MessageData message = mapper.readValue(record.value(), MessageData.class);
        MessageData response = null;
        try {
            switch (message.operation()) {
                case GET_ALL -> response = new MessageData(message.operation(), postKafkaController.getAll());
                case CREATE ->
                        response = new MessageData(message.operation(), Collections.singletonList(postKafkaController.create(message.requestTO())));
                case UPDATE ->
                        response = new MessageData(message.operation(), Collections.singletonList(postKafkaController.update(message.requestTO())));
                case GET_BY_ID ->
                        response = new MessageData(message.operation(), Collections.singletonList(postKafkaController.getById(message.itemId())));
                case DELETE_BY_ID -> postKafkaController.delete(message.itemId());
            }
            if(response == null){
                response = new MessageData(message.operation());
            }
        }catch (Exception e) {
            Logger.getAnonymousLogger().info(String.format("Caught exception:%s", e.getMessage()));
            response = new MessageData(new MessageData.ExceptionData(e.getClass().getSimpleName(),e.getMessage()));
        }
        sender.send(RESPONSE_TOPIC, uuid.toString(), mapper.writeValueAsString(response));
    }
}