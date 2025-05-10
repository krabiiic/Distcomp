package com.example.rv1.controller;

import com.example.rv1.dto.MessageDTO;
import com.example.rv1.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1.0/messages")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;
    @PostMapping
    public ResponseEntity<MessageDTO> createMessage(@Valid @RequestBody MessageDTO messageDTO) {
        MessageDTO dto = messageService.createMessage(messageDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageDTO> deleteMessage(@PathVariable int id) throws Exception {
        MessageDTO dto = messageService.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/{id}")
    public ResponseEntity<MessageDTO> getMessage(@PathVariable int id){
        MessageDTO dto = messageService.getMessage(id);
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }
    @GetMapping("")
    public ResponseEntity<List<MessageDTO>> getMessage(){
        List<MessageDTO> dto = messageService.getMessages();
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @PutMapping
    public ResponseEntity<MessageDTO> updateMessage(@Valid @RequestBody MessageDTO messageDTO){
        messageService.updateMessage(messageDTO);
        return ResponseEntity.status(HttpStatus.OK).body(messageDTO);
    }
}
