package by.zdanovich.Discussion.controllers;


import by.zdanovich.Discussion.DTOs.Requests.MessageRequestDTO;
import by.zdanovich.Discussion.DTOs.Responses.MessageResponseDTO;
import by.zdanovich.Discussion.services.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<MessageResponseDTO> getAllMessages() {
        return messageService.findAll();
    }

    @GetMapping("/{id}")
    public MessageResponseDTO getMessageById(@PathVariable Long id) {
        try {
            return messageService.findById(id);
        } catch (RuntimeException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public MessageResponseDTO updateMessage(@RequestBody @Valid MessageRequestDTO messageRequestDTO) {
        return messageService.update(messageRequestDTO);
    }
}
