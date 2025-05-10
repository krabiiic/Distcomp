package by.ryabchikov.tweet_service.controller;

import by.ryabchikov.tweet_service.dto.mark.MarkRequestTo;
import by.ryabchikov.tweet_service.dto.mark.MarkResponseTo;
import by.ryabchikov.tweet_service.dto.mark.MarkUpdateRequestTo;
import by.ryabchikov.tweet_service.service.MarkService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1.0/marks")
public class MarkController {
    private final MarkService markService;

    @GetMapping
    public ResponseEntity<List<MarkResponseTo>> readAll() {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(markService.readAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MarkResponseTo> readById(@PathVariable("id") @Valid @NotNull Long id) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(markService.readById(id));
    }

    @PostMapping
    public ResponseEntity<MarkResponseTo> create(@Valid @RequestBody MarkRequestTo markRequestTo) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(markService.create(markRequestTo));
    }

    @PutMapping
    public ResponseEntity<MarkResponseTo> update(@Valid @RequestBody MarkUpdateRequestTo markUpdateRequestTo) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(markService.update(markUpdateRequestTo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable("id") @Valid @NotNull Long id) {
        markService.deleteById(id);
        return ResponseEntity.noContent()
                .build();
    }
}
