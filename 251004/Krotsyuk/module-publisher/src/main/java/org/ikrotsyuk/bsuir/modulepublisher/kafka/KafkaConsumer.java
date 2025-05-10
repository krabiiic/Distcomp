package org.ikrotsyuk.bsuir.modulepublisher.kafka;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Component
public class KafkaConsumer {
    private final Map<String, CompletableFuture<Object>> pendingResponses = new ConcurrentHashMap<>();

    @KafkaListener(topics = KafkaConstants.OUT_TOPIC_NAME)
    public void listen(ConsumerRecord<String, Object> record) {
        String eventId = record.key();
        Object response = record.value();

        CompletableFuture<Object> future = pendingResponses.remove(eventId);
        if (future != null) {
            future.complete(response);
        }
    }

    public CompletableFuture<Object> waitForResponse(String eventId) {
        CompletableFuture<Object> future = new CompletableFuture<>();
        pendingResponses.put(eventId, future);
        return future;
    }
}
