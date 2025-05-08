package com.example.lab1.controller;

import com.example.lab1.dto.MarkerRequestTo;
import com.example.lab1.dto.MarkerResponseTo;
import com.example.lab1.service.MarkerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1.0/markers")
public class MarkerController {

    private final MarkerService markService;
    
    public MarkerController(MarkerService markService) {
        this.markService = markService;
    }
    
    @PostMapping
    public ResponseEntity<MarkerResponseTo> createMark(@Valid @RequestBody MarkerRequestTo request) {
        MarkerResponseTo response = markService.createMark(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @GetMapping
    public ResponseEntity<List<MarkerResponseTo>> getAllMarks() {
        return ResponseEntity.ok(markService.getAllMarks());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<MarkerResponseTo> getMarkById(@PathVariable Long id) {
        return ResponseEntity.ok(markService.getMarkById(id));
    }
    
    @PutMapping
    public ResponseEntity<MarkerResponseTo> updateMark(@Valid @RequestBody MarkerRequestTo request) {
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
