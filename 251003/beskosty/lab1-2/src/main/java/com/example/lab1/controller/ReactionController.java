package com.example.lab1.controller;

import com.example.lab1.dto.ReactionRequestTo;
import com.example.lab1.dto.ReactionResponseTo;
import com.example.lab1.service.ReactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1.0/reactions")
public class ReactionController {

    private final ReactionService reactionService;
    
    public ReactionController(ReactionService reactionService) {
        this.reactionService = reactionService;
    }
    
    @PostMapping
    public ResponseEntity<ReactionResponseTo> createReaction(@Valid @RequestBody ReactionRequestTo request) {
        ReactionResponseTo response = reactionService.createReaction(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @GetMapping
    public ResponseEntity<List<ReactionResponseTo>> getAllReactions() {
        return ResponseEntity.ok(reactionService.getAllReactions());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ReactionResponseTo> getReactionById(@PathVariable Long id) {
        return ResponseEntity.ok(reactionService.getReactionById(id));
    }
    
    @PutMapping
    public ResponseEntity<ReactionResponseTo> updateReaction(@Valid @RequestBody ReactionRequestTo request) {
        if(request.getId() == null) {
            throw new IllegalArgumentException("ID must be provided");
        }
        return ResponseEntity.ok(reactionService.updateReaction(request.getId(), request));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReaction(@PathVariable Long id) {
        reactionService.deleteReaction(id);
        return ResponseEntity.noContent().build();
    }
}
