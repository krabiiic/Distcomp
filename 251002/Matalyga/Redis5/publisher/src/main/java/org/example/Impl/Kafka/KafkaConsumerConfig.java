package org.example.Impl.Kafka;

import org.example.Impl.DTO.PostResponseTo;
import org.example.Impl.DTO.PostResponseToSerializer;
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
    public KafkaConsumer<String, PostResponseTo> kafkaPostConsumer() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, PostResponseToSerializer.class.getName());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "OutTopic");
        KafkaConsumer<String, PostResponseTo> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList("OutTopic"));
        return consumer;
    }

}
