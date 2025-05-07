package com.example.publisher.controller;

import com.example.publisher.dto.TopicRequestTo;
import com.example.publisher.dto.TopicResponseTo;
import com.example.publisher.service.TopicService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1.0/news")
@RequiredArgsConstructor
public class TopicController {
    private final TopicService topicService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<TopicResponseTo> getTopics() {
        return topicService.getAll();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public TopicResponseTo getTopic(@PathVariable long id) {
        return topicService.getById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public TopicResponseTo createTopic(@Valid @RequestBody TopicRequestTo topicRequestTo) {
        return topicService.create(topicRequestTo);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping
    public TopicResponseTo updateTopic(@Valid @RequestBody TopicRequestTo topicRequestTo) {
        return topicService.update(topicRequestTo);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteTopic(@PathVariable Long id) {
        topicService.deleteById(id);
    }
}
