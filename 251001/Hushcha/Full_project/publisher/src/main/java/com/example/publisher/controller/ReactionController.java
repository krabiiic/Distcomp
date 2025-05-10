package com.example.publisher.controller;

import com.example.publisher.dto.ReactionRequestTo;
import com.example.publisher.dto.ReactionResponseTo;
import com.example.publisher.service.ReactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/api/v1.0/notes")
@RequiredArgsConstructor
public class ReactionController {

    private final ReactionService reactionService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ReactionResponseTo createReaction(@RequestBody ReactionRequestTo reactionRequestTo) {
        return reactionService.createReaction(reactionRequestTo);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public ReactionResponseTo getReaction(@PathVariable Long id) {
        return reactionService.getReactionById(id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteReaction(@PathVariable Long id) {
        reactionService.deleteReaction(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<ReactionResponseTo> getAllReactions() {
        return StreamSupport.stream(reactionService.getAllReactions().spliterator(), false)
                .collect(Collectors.toList());
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping
    public ReactionResponseTo updateReaction(@RequestBody ReactionRequestTo reactionRequestTo) {
        return reactionService.updateReaction(reactionRequestTo);
    }
}

