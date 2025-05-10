package org.ikrotsyuk.bsuir.modulepublisher.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ikrotsyuk.bsuir.modulepublisher.dto.request.ReactionRequestDTO;
import org.ikrotsyuk.bsuir.modulepublisher.dto.response.ReactionResponseDTO;
import org.ikrotsyuk.bsuir.modulepublisher.service.ReactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/api/v1.0/reactions")
public class ReactionController {
    private final ReactionService reactionService;

    @GetMapping
    public ResponseEntity<List<ReactionResponseDTO>> getReactions(){
        return new ResponseEntity<>(reactionService.getReactions(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReactionResponseDTO> getReaction(@PathVariable Long id){
        return new ResponseEntity<>(reactionService.getReactionById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ReactionResponseDTO> addReaction(@Valid @RequestBody ReactionRequestDTO reactionRequestDTO){
        return new ResponseEntity<>(reactionService.addReaction(reactionRequestDTO), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ReactionResponseDTO> deleteReaction(@PathVariable Long id){
        return new ResponseEntity<>(reactionService.deleteReaction(id), HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReactionResponseDTO> updateReaction(@PathVariable Long id, @Valid @RequestBody ReactionRequestDTO reactionRequestDTO){
        return new ResponseEntity<>(reactionService.updateReaction(id, reactionRequestDTO), HttpStatus.OK);
    }
}
