package org.ikrotsyuk.bsuir.modulepublisher.client;

import jakarta.validation.Valid;
import org.ikrotsyuk.bsuir.modulepublisher.dto.request.ReactionRequestDTO;
import org.ikrotsyuk.bsuir.modulepublisher.dto.response.ReactionResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "module-discussion", url = "localhost:24130", configuration = {CustomErrorDecoder.class})
public interface ReactionClient {
    @GetMapping("/api/v1.0/reactions")
    ResponseEntity<List<ReactionResponseDTO>> getReactions();

    @GetMapping("/api/v1.0/reactions/{id}")
    ResponseEntity<ReactionResponseDTO> getReaction(@PathVariable Long id);

    @PostMapping("/api/v1.0/reactions")
    ResponseEntity<ReactionResponseDTO> addReaction(@Valid @RequestBody ReactionRequestDTO reactionRequestDTO);

    @DeleteMapping("/api/v1.0/reactions/{id}")
    ResponseEntity<ReactionResponseDTO> deleteReaction(@PathVariable Long id);

    @PutMapping("/api/v1.0/reactions/{id}")
    ResponseEntity<ReactionResponseDTO> updateReaction(@PathVariable Long id, @Valid @RequestBody ReactionRequestDTO reactionRequestDTO);
}
