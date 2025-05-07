package by.yelkin.TopicService.controller;

import by.yelkin.TopicService.service.TopicService;
import by.yelkin.api.topic.api.TopicApi;
import by.yelkin.api.topic.dto.TopicRq;
import by.yelkin.api.topic.dto.TopicRs;
import by.yelkin.api.topic.dto.TopicUpdateRq;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class TopicController implements TopicApi {
    private final TopicService topicService;

    @Override
    public ResponseEntity<TopicRs> create(TopicRq rq) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(topicService.create(rq));
    }

    @Override
    public ResponseEntity<TopicRs> readById(Long id) {
        return ResponseEntity.ok().body(topicService.readById(id));
    }

    @Override
    public ResponseEntity<List<TopicRs>> readAll() {
        return ResponseEntity.ok().body(topicService.readAll());
    }

    @Override
    public ResponseEntity<TopicRs> update(TopicUpdateRq rq) {
        return ResponseEntity.ok().body(topicService.update(rq));
    }

    @Override
    public ResponseEntity<Void> deleteById(Long id) {
        topicService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}