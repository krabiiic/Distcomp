package com.rita.publisher.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.errors.TimeoutException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;
@Component
public class KafkaClient {
    public static final String REQUEST_TOPIC = "InTopic";
    public static final String RESPONSE_TOPIC = "OutTopic";
    @Autowired
    private KafkaTemplate<String, String> sender;
    @Autowired
    private ObjectMapper json;
    @Autowired
    private ConcurrentHashMap<UUID, Exchanger<MessageEvent>> kafkaCache ;
    @SneakyThrows
    public MessageEvent sync (MessageEvent messageEvent) {
        UUID uuid = messageEvent.mapKey();
        Exchanger<MessageEvent> exchanger = new Exchanger<>();
        kafkaCache.put (uuid, exchanger);
        String kafkaKey =   uuid.toString();
        if(messageEvent.message()!=null)
            kafkaKey=messageEvent.message().tweetId().toString();
        try {
            sender.send(REQUEST_TOPIC, kafkaKey, json.writeValueAsString(messageEvent)); return exchanger.exchange(messageEvent,1, TimeUnit.SECONDS);
        } catch (TimeoutException e){
            kafkaCache.remove (uuid);
            throw e;
        }
    }
    @SneakyThrows
    @KafkaListener(topics = RESPONSE_TOPIC, groupId = "groupId=#{T(java.util.UUID).randomUUID().toString()}")
    private void processMessage (ConsumerRecord<String, String> record) {
        MessageEvent result = json.readValue(record.value(), MessageEvent.class);

        Exchanger<MessageEvent> exchanger = kafkaCache.remove(result.mapKey());
        if (exchanger!=null) {
            exchanger.exchange(result,1, TimeUnit.SECONDS);
        }
    }
}
