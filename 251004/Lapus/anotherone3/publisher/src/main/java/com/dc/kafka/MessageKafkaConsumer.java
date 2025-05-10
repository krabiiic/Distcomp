package com.dc.kafka;

import com.dc.model.kafka.MessageKafka;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class MessageKafkaConsumer {

    @Autowired
    private KafkaTemplate<Long, MessageKafka> kafkaTemplate;
    private final Map<String, CompletableFuture<MessageKafka>> futures = new ConcurrentHashMap<>();

    public MessageKafka requestNews(Long newsId) throws Exception {
        String correlationId = UUID.randomUUID().toString();
        CompletableFuture<MessageKafka> future = new CompletableFuture<>();

        futures.put(correlationId, future);

        MessageKafka message = new MessageKafka();
        message.setNewsId(newsId);
        message.setPurpose("getAll");
        message.setCorrelationId(correlationId);

        kafkaTemplate.send("InTopic", newsId, message);

        try {
            return future.get(10, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            futures.remove(correlationId);
            throw new RuntimeException("Timeout while waiting for response");
        }
    }

    @KafkaListener(topics = "InTopic", groupId = "publisher-group")
    public void completeResponse(MessageKafka message) {
        String correlationId = message.getCorrelationId();
        CompletableFuture<MessageKafka> future = futures.remove(correlationId);
        if (future != null) {
            future.complete(message);
        }
    }
}
