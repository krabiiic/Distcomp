package com.example.publisher.controller;

import com.example.publisher.dto.ReactionRequestTo;
import com.example.publisher.dto.ReactionResponseTo;
import com.example.publisher.service.ReactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1.0/reactions")
@RequiredArgsConstructor
public class ReactionController {

    private final ReactionService reactionService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ReactionResponseTo createReaction(@RequestBody ReactionRequestTo reactionRequestTo) {
        return reactionService.createReaction(reactionRequestTo).block();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public ReactionResponseTo getReaction(@PathVariable Long id) {
        return reactionService.getReactionById(id).block();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteReaction(@PathVariable Long id) {
        reactionService.deleteReaction(id).block();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<ReactionResponseTo> getAllReactions() {
        return reactionService.getAllReactions()
                .collectList()
                .block();
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping
    public ReactionResponseTo updateReaction(@RequestBody ReactionRequestTo reactionRequestTo) {
        return reactionService.updateReaction(reactionRequestTo);
    }
}

