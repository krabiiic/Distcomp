package com.discussion.rvlab4_discussion.controller;

import com.discussion.rvlab4_discussion.dto.MessageRequestTo;
import com.discussion.rvlab4_discussion.dto.MessageResponseTo;
import com.discussion.rvlab4_discussion.service.MessageService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1.0/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<MessageResponseTo> createMessage(@RequestBody MessageRequestTo messageRequestTo) {
        try {
            if (!validateMessageData(messageRequestTo)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "{}");
            }

            MessageResponseTo response = messageService.createMessage(messageRequestTo);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
        catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "{}");
        }
    }

    @GetMapping
    public List<MessageResponseTo> getAllMessages() {
        return messageService.getAllMessages();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getMessageById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(messageService.getMessageById(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{}");
        }
    }

    @PutMapping
    public ResponseEntity<MessageResponseTo> updateMessage(@RequestBody MessageRequestTo messageRequestTo) {
        System.out.println("Received PUT request with ID: " + messageRequestTo.getId());
        try {
            if (messageRequestTo.getId() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponseTo());
            }
            MessageResponseTo updatedMessage = messageService.updateMessage(messageRequestTo.getId(), messageRequestTo);
            return ResponseEntity.ok(updatedMessage);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponseTo());
        }
    }

    private boolean validateMessageData(MessageRequestTo messageRequestTo) {

        if (messageRequestTo.getContent().length() < 2 ||
                messageRequestTo.getContent().length() > 2048) {
            return false;
        }

        return true;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id) {
        try {
            messageService.deleteMessage(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
