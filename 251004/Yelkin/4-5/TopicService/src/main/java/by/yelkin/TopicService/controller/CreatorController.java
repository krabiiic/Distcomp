package by.yelkin.TopicService.controller;

import by.yelkin.TopicService.dto.creator.CreatorRequestTo;
import by.yelkin.TopicService.dto.creator.CreatorResponseTo;
import by.yelkin.TopicService.dto.creator.CreatorUpdate;
import by.yelkin.TopicService.exceptionHandler.CreatorNotFoundException;
import by.yelkin.TopicService.service.CreatorService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Validated
@RestController
@RequestMapping("/api/v1.0/creators")
public class CreatorController {

    private final CreatorService creatorService;

    public CreatorController(CreatorService creatorService) {
        this.creatorService = creatorService;
    }

    @GetMapping
    public ResponseEntity<List<CreatorResponseTo>> findAll() {
        return ResponseEntity.ok(creatorService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CreatorResponseTo> findById(@PathVariable @Valid @NotNull Long id) {
        Optional<CreatorResponseTo> creator = creatorService.findById(id);
        return creator.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CreatorResponseTo> create(@Valid @RequestBody CreatorRequestTo creatorRequestTo) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(creatorService.create(creatorRequestTo));

    }

    @PutMapping()
    public ResponseEntity<CreatorResponseTo> update(@Valid @RequestBody CreatorUpdate creatorUpdate) {
        return ResponseEntity.ok(creatorService.update(creatorUpdate));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @Valid @NotNull Long id) {
        creatorService.findById(id).orElseThrow(() -> new CreatorNotFoundException(id));
        creatorService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
