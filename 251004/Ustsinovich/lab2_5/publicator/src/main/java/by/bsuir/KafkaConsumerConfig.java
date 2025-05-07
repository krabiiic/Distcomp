package by.bsuir;

import by.bsuir.dto.NoteResponseTo;
import by.bsuir.dto.NoteResponseToSerializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {
    @Bean
    public KafkaConsumer<String, NoteResponseTo> kafkaNoteConsumer() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9093");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, NoteResponseToSerializer.class.getName());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "OutTopic");
        KafkaConsumer<String, NoteResponseTo> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList("OutTopic"));
        return consumer;
    }

}
