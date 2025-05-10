package com.example.restservice.kafka;

import com.example.restservice.dto.kafka.MessageEvent;
import com.example.restservice.dto.request.MessageRequestTo;
import com.example.restservice.model.Message;
import com.example.restservice.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KafkaModerationConsumer {
    private final MessageService messageService;
    private final KafkaTemplate<String, MessageEvent> kafkaTemplate;
    private final List<String> stopWords = List.of("spam", "scam");

    @KafkaListener(topics = "InTopic", groupId = "publisher-group", containerFactory = "kafkaListenerContainerFactory")
    public void moderateMessage(MessageEvent event) {
        switch (event.getType()) {
            case "create" ->
                    messageService.create(new MessageRequestTo(event.getId(), event.getArticleId(), event.getContent()));
            case "update" -> {
                messageService.update(new MessageRequestTo(event.getId(), event.getArticleId(), event.getContent()));
            }
            case "delete" -> {
                messageService.delete(event.getId());
                return;
            }
        }

        Message.MessageState state = containsStopWords(event.getContent())
                ? Message.MessageState.DECLINE
                : Message.MessageState.APPROVE;
        messageService.updateState(event.getId(), state);

        event.setState(state);
        kafkaTemplate.send("OutTopic", event.getArticleId().toString(), event);
    }

    private boolean containsStopWords(String content) {
        return stopWords.stream().anyMatch(content::contains);
    }
}