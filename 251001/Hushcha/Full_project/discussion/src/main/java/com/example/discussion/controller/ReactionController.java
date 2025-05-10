package com.example.discussion.controller;

import com.example.discussion.dto.ReactionRequestTo;
import com.example.discussion.dto.ReactionResponseTo;
import com.example.discussion.service.ReactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1.0/notes")
public class ReactionController {

    @Autowired
    private final ReactionService reactionService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ReactionResponseTo create(@RequestBody ReactionRequestTo request) {
        return reactionService.create(request);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<ReactionResponseTo> getAll() {
        return reactionService.getAll();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public ReactionResponseTo getById(@PathVariable Long id) {
        return reactionService.getById(id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        reactionService.deleteById(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping
    public ReactionResponseTo updateReaction(@RequestBody ReactionRequestTo reactionRequestTo) {
        return reactionService.updateReaction(reactionRequestTo);
    }
}