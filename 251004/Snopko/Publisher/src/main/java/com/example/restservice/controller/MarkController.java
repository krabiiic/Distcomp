package com.example.restservice.controller;

import com.example.restservice.dto.request.MarkRequestTo;
import com.example.restservice.dto.response.MarkResponseTo;
import com.example.restservice.service.MarkService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1.0/marks")
public class MarkController {
    private final MarkService markService;

    @GetMapping
    public ResponseEntity<List<MarkResponseTo>> getAll() {
        return ResponseEntity.ok(markService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MarkResponseTo> getById(@PathVariable Long id) {
        return ResponseEntity.ok(markService.getById(id));
    }

    @PostMapping
    public ResponseEntity<MarkResponseTo> create(@RequestBody @Valid MarkRequestTo request) {
      return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(markService.create(request));
    }

    @PutMapping
    public ResponseEntity<MarkResponseTo> update(@RequestBody @Valid MarkRequestTo request) {
        return ResponseEntity.ok(markService.update(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        markService.delete(id);
        return ResponseEntity.noContent().build();
    }
}