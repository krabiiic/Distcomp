package com.example.lab1.controller;

import com.example.lab1.dto.MarkRequestTo;
import com.example.lab1.dto.MarkResponseTo;
import com.example.lab1.service.MarkService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1.0/marks")
public class MarkController {

    private final MarkService markService;
    
    public MarkController(MarkService markService) {
        this.markService = markService;
    }
    
    @PostMapping
    public ResponseEntity<MarkResponseTo> createMark(@Valid @RequestBody MarkRequestTo request) {
        MarkResponseTo response = markService.createMark(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @GetMapping
    public ResponseEntity<List<MarkResponseTo>> getAllMarks() {
        return ResponseEntity.ok(markService.getAllMarks());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<MarkResponseTo> getMarkById(@PathVariable Long id) {
        return ResponseEntity.ok(markService.getMarkById(id));
    }
    
    @PutMapping
    public ResponseEntity<MarkResponseTo> updateMark(@Valid @RequestBody MarkRequestTo request) {
        if(request.getId() == null) {
            throw new IllegalArgumentException("ID must be provided");
        }
        return ResponseEntity.ok(markService.updateMark(request.getId(), request));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMark(@PathVariable Long id) {
        markService.deleteMark(id);
        return ResponseEntity.noContent().build();
    }
}
