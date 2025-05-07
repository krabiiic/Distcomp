package org.ikrotsyuk.bsuir.modulepublisher.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.ikrotsyuk.bsuir.modulepublisher.kafka.KafkaConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    Map<String, Object> producerConfigs(){
        Map<String, Object> config = new HashMap<>();

        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConstants.BOOTSTRAP_SERVERS);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, KafkaConstants.KEY_SERIALIZER);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaConstants.VALUE_SERIALIZER);
        config.put(ProducerConfig.ACKS_CONFIG, KafkaConstants.ACKNOWLEDGEMENTS_POLICY);
        config.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, KafkaConstants.DELIVERY_TIMEOUT_MS);
        config.put(ProducerConfig.LINGER_MS_CONFIG, KafkaConstants.LINGER_MS);
        config.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, KafkaConstants.REQUEST_TIMEOUT_MS);
        config.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, KafkaConstants.ENABLE_IDEMPOTENCE);
        config.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, KafkaConstants.MAX_IN_FLIGHT_REQUESTS_PER_SECOND);

        return config;
    }

    @Bean
    ProducerFactory<String, Object> modifyProducerFactory(){
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    KafkaTemplate<String, Object> modifyKafkaTemplate(){
        return new KafkaTemplate<>(modifyProducerFactory());
    }

    @Bean
    public NewTopic createInTopic(){
        return TopicBuilder.name(KafkaConstants.IN_TOPIC_NAME)
                .partitions(KafkaConstants.NUMBER_OF_PARTITIONS)
                .build();
    }

    @Bean
    ConsumerFactory<String, Object> consumerFactory() {
        Map<String, Object> config = new HashMap<>();

        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConstants.BOOTSTRAP_SERVERS);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        config.put(JsonDeserializer.TRUSTED_PACKAGES, KafkaConstants.TRUSTED_PACKAGES);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, KafkaConstants.CONSUMER_GROUP_ID);

        return new DefaultKafkaConsumerFactory<>(config);
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory(
            ConsumerFactory<String, Object> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);

        return factory;
    }
}
