package org.ex.distributed_computing.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.ex.distributed_computing.dto.request.ReactionRequestDTO;
import org.ex.distributed_computing.dto.response.ReactionResponseDTO;
import org.ex.distributed_computing.service.ReactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reactions")
@RequiredArgsConstructor
public class ReactionController {

  private final ReactionService reactionService;

  @GetMapping
  public List<ReactionResponseDTO> getAllReactions() {
    return reactionService.getAllReactions();
  }

  @GetMapping("/{id}")
  @SneakyThrows
  public ResponseEntity<ReactionResponseDTO> getReactionById(@PathVariable Long id) {
    Thread.sleep(300);
    return ResponseEntity.ok(reactionService.getReactionById(id));
  }

  @PostMapping
  public ResponseEntity<ReactionResponseDTO> createReaction(@Valid @RequestBody ReactionRequestDTO requestDTO) {
    return ResponseEntity.status(HttpStatus.CREATED).body(reactionService.createReaction(requestDTO));
  }

  @PutMapping
  public ResponseEntity<ReactionResponseDTO> updateReaction(@Valid @RequestBody ReactionRequestDTO requestDTO) {
    return ResponseEntity.ok(reactionService.updateReaction(requestDTO));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteReaction(@PathVariable Long id) {
    reactionService.deleteReaction(id);
    return ResponseEntity.noContent().build();
  }
}

