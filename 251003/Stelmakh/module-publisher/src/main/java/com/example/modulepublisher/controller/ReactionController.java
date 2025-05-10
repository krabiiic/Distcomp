package com.example.modulepublisher.controller;

import com.example.modulepublisher.kafka.KafkaProducerService;
import com.example.modulepublisher.dto.ReactionDTO;
import com.example.modulepublisher.service.ReactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1.0/reactions")
@RequiredArgsConstructor
public class ReactionController {
    private final ReactionService messageService;
    private final KafkaProducerService kafkaProducerService;
    @PostMapping
    public ResponseEntity<ReactionDTO> createReaction(@Valid @RequestBody ReactionDTO userDTO) {
        ReactionDTO dto = messageService.createReaction(userDTO);
        dto.setAction("CREATE");
        kafkaProducerService.sendMessage("InTopic", dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ReactionDTO> deleteReaction(@PathVariable int id) throws Exception {
        ReactionDTO dto = messageService.deleteReaction(id);
        dto.setAction("DELETE");
        kafkaProducerService.sendMessage("InTopic", dto);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/{id}")
    public ResponseEntity<ReactionDTO> getReaction(@PathVariable int id){
        ReactionDTO dto = messageService.getReaction(id);
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }
    @GetMapping("")
    public ResponseEntity<List<ReactionDTO>> getReaction(){
        List<ReactionDTO> dto = messageService.getReactions();
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @PutMapping
    public ResponseEntity<ReactionDTO> updateReaction(@Valid @RequestBody ReactionDTO userDTO){
        messageService.updateReaction(userDTO);
        return ResponseEntity.status(HttpStatus.OK).body(userDTO);
    }
}
