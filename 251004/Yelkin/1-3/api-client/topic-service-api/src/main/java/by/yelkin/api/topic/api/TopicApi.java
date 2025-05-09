package by.yelkin.api.topic.api;

import by.yelkin.api.topic.dto.TopicRq;
import by.yelkin.api.topic.dto.TopicRs;
import by.yelkin.api.topic.dto.TopicUpdateRq;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface TopicApi {
    @PostMapping("/api/v1.0/topics")
    ResponseEntity<TopicRs> create(@Valid @RequestBody TopicRq rq);

    @GetMapping("/api/v1.0/topics/{id}")
    ResponseEntity<TopicRs> readById(@Valid @NotNull @PathVariable("id") Long id);

    @GetMapping("/api/v1.0/topics")
    ResponseEntity<List<TopicRs>> readAll();

    @PutMapping("/api/v1.0/topics")
    ResponseEntity<TopicRs> update(@Valid @RequestBody TopicUpdateRq rq);

    @DeleteMapping("/api/v1.0/topics/{id}")
    ResponseEntity<Void> deleteById(@Valid @NotNull @PathVariable("id") Long id);
}
