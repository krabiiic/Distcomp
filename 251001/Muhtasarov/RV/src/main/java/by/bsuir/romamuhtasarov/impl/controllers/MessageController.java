package by.bsuir.romamuhtasarov.impl.controllers;

import by.bsuir.romamuhtasarov.impl.service.MessageService;
import by.bsuir.romamuhtasarov.impl.dto.MessageResponseTo;
import by.bsuir.romamuhtasarov.impl.dto.MessageRequestTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1.0")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @GetMapping("/messages")
    public ResponseEntity<List<MessageResponseTo>> getAllMessages() {
        List<MessageResponseTo> messageResponseToList = messageService.getAll();
        return new ResponseEntity<>(messageResponseToList, HttpStatus.OK);
    }

    @GetMapping("/messages/{id}")
    public ResponseEntity<MessageResponseTo> getMessage(@PathVariable long id) {
        MessageResponseTo MessageResponseTo = messageService.get(id);
        return new ResponseEntity<>(MessageResponseTo, MessageResponseTo == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
    }

    @PostMapping("/messages")
    public ResponseEntity<MessageResponseTo> createMessage(@RequestBody MessageRequestTo MessageTo) {
        MessageResponseTo addedMessage = messageService.add(MessageTo);
        return new ResponseEntity<>(addedMessage, HttpStatus.CREATED);
    }

    @DeleteMapping("/messages/{id}")
    public ResponseEntity<MessageResponseTo> deleteMessage(@PathVariable long id) {
        MessageResponseTo deletedMessage = messageService.delete(id);
        return new ResponseEntity<>(deletedMessage, deletedMessage == null ? HttpStatus.NOT_FOUND : HttpStatus.NO_CONTENT);
    }

    @PutMapping("/messages")
    public ResponseEntity<MessageResponseTo> updateMessage(@RequestBody MessageRequestTo MessageRequestTo) {
        MessageResponseTo MessageResponseTo = messageService.update(MessageRequestTo);
        return new ResponseEntity<>(MessageResponseTo, MessageResponseTo.getContent() == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
    }
}