package com.example.modulediscussion.controller;

import com.example.modulediscussion.dto.MessageDTO;
import com.example.modulediscussion.service.MessageService;
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
    public ResponseEntity<MessageDTO> createUser(@Valid @RequestBody MessageDTO userDTO) {
        MessageDTO dto = messageService.createMessage(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageDTO> deleteUser(@PathVariable int id) throws Exception {
        MessageDTO dto = messageService.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/{id}")
    public ResponseEntity<MessageDTO> getUser(@PathVariable int id){
        MessageDTO dto = messageService.getMessage(id);
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }
    @GetMapping("")
    public ResponseEntity<List<MessageDTO>> getUser(){
        List<MessageDTO> dto = messageService.getMessages();
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @PutMapping
    public ResponseEntity<MessageDTO> updateUser(@Valid @RequestBody MessageDTO userDTO){
        messageService.updateMessage(userDTO);
        return ResponseEntity.status(HttpStatus.OK).body(userDTO);
    }
}
