package by.bsuir.controllers;

import by.bsuir.dto.NoteRequestTo;
import by.bsuir.dto.NoteResponseTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaSender {
    @Autowired
    private KafkaTemplate<String, NoteRequestTo> userKafkaTemplate;


    void sendCustomMessage(NoteRequestTo noteRequestTo, String topicName) {
        userKafkaTemplate.send(topicName, noteRequestTo);
    }
}
