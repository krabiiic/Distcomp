package com.lab.labDCP.controller;

import com.lab.labDCP.dto.NoticeRequestTo;
import com.lab.labDCP.service.KafkaConsumerService;
import com.lab.labDCP.service.KafkaProducerService;
import com.lab.labDCP.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1.0/notices")
public class NoticeControllerKafka {

    private final KafkaProducerService kafkaProducerService;
    private final KafkaConsumerService kafkaConsumerService;
    private final RedisService redisService;

    @Autowired
    public NoticeControllerKafka(KafkaProducerService kafkaProducerService,
                                 KafkaConsumerService kafkaConsumerService,
                                 RedisService redisService) {
        this.kafkaProducerService = kafkaProducerService;
        this.kafkaConsumerService = kafkaConsumerService;
        this.redisService = redisService;
    }

    @GetMapping
    public ResponseEntity<Object> getAllNotices() {
        String redisKey = "notice:all";

        try {
            Object cachedNotices = redisService.getValue(redisKey);
            if (cachedNotices != null) {
                return ResponseEntity.ok(cachedNotices);
            }

            kafkaProducerService.sendMessage("In Topic", "Get all");
            Object response = kafkaConsumerService.waitForMessage(100);

            if (response != null) {
                redisService.setValueWithExpireSeconds(redisKey, response, 3600);
            }

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error fetching notices", "details", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getNoticeById(@PathVariable String id) {
        String redisKey = "notice:" + id;

        try {
            Object cachedNotice = redisService.getValue(redisKey);
            if (cachedNotice != null) {
                return ResponseEntity.ok(cachedNotice);
            }

            kafkaProducerService.sendMessage("In Topic", "Get by id " + id);
            Object response = kafkaConsumerService.waitForMessage(1000);

            if (response != null) {
                redisService.setValueWithExpireSeconds(redisKey, response, 3600);
            }

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error fetching notice", "id", id, "details", e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<Object> createNotice(@RequestBody NoticeRequestTo newNotice) {
        try {

            kafkaProducerService.sendMessage("In Topic", "Post " + newNotice.toString());
            Object response = kafkaConsumerService.waitForMessage(1000);

            if (response != null) {
                String id = extractIdFromResponse(response);
                String redisKey = "notice:" + id;
                redisService.setValueWithExpireSeconds(redisKey, response, 3600);
                redisService.deleteValue("notice:all");
            }

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error creating notice", "details", e.getMessage()));
        }
    }

    @PutMapping
    public ResponseEntity<Object> updateNotice(@RequestBody NoticeRequestTo updatedNotice) {
        try {
            kafkaProducerService.sendMessage("In Topic", "Put " + updatedNotice.toString());
            Object response = kafkaConsumerService.waitForMessage(1000);

            if (response != null) {
                String redisKey = "notice:" + updatedNotice.getId();
                redisService.setValueWithExpireSeconds(redisKey, response, 3600);
                redisService.deleteValue("notice:all");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error updating notice", "id", updatedNotice.getId(), "details", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteNotice(@PathVariable Long id) {
        try {
            kafkaProducerService.sendMessage("In Topic", "Delete " + id);
            Object response = kafkaConsumerService.waitForMessage(1000);

            if (response != null) {
                redisService.deleteValue("notice:" + id);
                redisService.deleteValue("notice:all");
            }

            return ResponseEntity.noContent().build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error deleting notice", "id", id, "details", e.getMessage()));
        }
    }

    private String extractIdFromResponse(Object response) {
        if (response instanceof Map) {
            return ((Map<?, ?>) response).get("id").toString();
        }
        throw new RuntimeException("Cannot extract ID from response");
    }
}