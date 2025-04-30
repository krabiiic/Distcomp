package com.example.restservice.controller;

import com.example.restservice.dto.kafka.MessageEvent;
import com.example.restservice.dto.request.MessageRequestTo;
import com.example.restservice.dto.response.MessageResponseTo;
import com.example.restservice.kafka.KafkaMessageProducer;
import com.example.restservice.service.MessageService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1.0/messages")
public class MessageController {
    private final MessageService messageService;
    //private final WebClient webClient;

    @GetMapping
    public ResponseEntity<List<MessageResponseTo>> getAll() {
        return ResponseEntity.ok(messageService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MessageResponseTo> getById(@PathVariable Long id) {
        return ResponseEntity.ok(messageService.getById(id));
    }

    @PostMapping
    public ResponseEntity<MessageResponseTo> create(@RequestBody @Valid MessageRequestTo request) {
        MessageResponseTo response = messageService.create(request);
        //sendPostRequest(response);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping
    public ResponseEntity<MessageResponseTo> update(@RequestBody @Valid MessageRequestTo request) {
        MessageResponseTo response = messageService.update(request);
        //sendPutRequest(response);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        messageService.delete(id);
        //sendDeleteRequest(id);
        return ResponseEntity.noContent().build();
    }

    /*

    private final String url = "http://localhost:24130/api/v1.0/messages";

    private void sendPostRequest(MessageResponseTo body) {
        webClient.post()
                .uri(url)
                .bodyValue(body)
                .retrieve()
                .toBodilessEntity()
                .subscribe();
    }

    private void sendPutRequest(MessageResponseTo body) {
        webClient.put()
                .uri(url)
                .bodyValue(body)
                .retrieve()
                .toBodilessEntity()
                .subscribe();
    }

    private void sendDeleteRequest(Long id) {
        webClient.delete()
                .uri(url + id.toString())
                .retrieve()
                .toBodilessEntity()
                .subscribe();
    }
     */
}