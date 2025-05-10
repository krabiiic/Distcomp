package by.yelkin.TopicService.controller;


import by.yelkin.TopicService.dto.mark.MarkRequestTo;
import by.yelkin.TopicService.dto.mark.MarkResponseTo;

import by.yelkin.TopicService.dto.mark.MarkUpdate;
import by.yelkin.TopicService.exceptionHandler.CreatorNotFoundException;
import by.yelkin.TopicService.service.MarkService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1.0/tags")
public class TagController {

    private final MarkService markService;

    public TagController(MarkService markService) {
        this.markService = markService;
    }

    @GetMapping
    public ResponseEntity<List<MarkResponseTo>> findAll() {
        return ResponseEntity.ok(markService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MarkResponseTo> findById(@PathVariable Long id) {
        Optional<MarkResponseTo> creator = markService.findById(id);
        return creator.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<MarkResponseTo> create(@Valid @RequestBody MarkRequestTo creatorRequestTo) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(markService.create(creatorRequestTo));

    }

    @PutMapping()
    public ResponseEntity<MarkResponseTo> update(@Valid @RequestBody MarkUpdate creatorUpdate) {
        return ResponseEntity.ok(markService.update(creatorUpdate));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        markService.findById(id).orElseThrow(() -> new CreatorNotFoundException(id));
        markService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
