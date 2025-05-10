package by.yelkin.TopicService.controller;

import by.yelkin.TopicService.dto.topic.TopicRequestTo;
import by.yelkin.TopicService.dto.topic.TopicResponseTo;
import by.yelkin.TopicService.dto.topic.TopicUpdate;
import by.yelkin.TopicService.exceptionHandler.CreatorNotFoundException;
import by.yelkin.TopicService.service.TopicService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Validated
@RestController
@RequestMapping("/api/v1.0/topics")
public class TopicController {

    private final TopicService topicService;

    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    @GetMapping
    public ResponseEntity<List<TopicResponseTo>> findAll() {
        return ResponseEntity.ok(topicService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TopicResponseTo> findById(@PathVariable Long id) {
        Optional<TopicResponseTo> topic = topicService.findById(id);
        return topic.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TopicResponseTo> create(@Valid @RequestBody TopicRequestTo topicRequestTo) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(topicService.create(topicRequestTo));

    }

    @PutMapping()
    public ResponseEntity<TopicResponseTo> update(@Valid @RequestBody TopicUpdate topicUpdate) {
        return ResponseEntity.ok(topicService.update(topicUpdate));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        topicService.findById(id).orElseThrow(() -> new CreatorNotFoundException(id));
        topicService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
