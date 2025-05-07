package by.bsuir.controllers;

import by.bsuir.dto.NoteRequestTo;
import by.bsuir.dto.NoteResponseTo;
import by.bsuir.services.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1.0/notes")
public class NoteController {
    @Autowired
    private NoteService noteService;
    @Autowired
    private KafkaSender kafkaSender;
    private String topic = "OutTopic";

    @KafkaListener(topics = "InTopic", groupId = "inGroup",
            containerFactory = "noteRequestToConcurrentKafkaListenerContainerFactory")
    void listenerWithMessageConverter(@Payload NoteRequestTo noteRequestTo) {
        if (Objects.equals(noteRequestTo.getMethod(), "GET")) {
            if (noteRequestTo.getId() != null) {
                kafkaSender.sendCustomMessage(getNote(noteRequestTo.getId()), topic);
            } else {
               // kafkaSender.sendCustomMessage(getNotes());
            }
        } else {
            if (Objects.equals(noteRequestTo.getMethod(), "DELETE")) {
                kafkaSender.sendCustomMessage(deleteNote(noteRequestTo.getId()), topic);
            } else {
                if (Objects.equals(noteRequestTo.getMethod(), "POST")) {
                    kafkaSender.sendCustomMessage(saveNote(noteRequestTo.getCountry(), noteRequestTo), topic);
                } else {
                    if (Objects.equals(noteRequestTo.getMethod(), "PUT")) {
                        kafkaSender.sendCustomMessage(updateNote(noteRequestTo.getCountry(), noteRequestTo), topic);
                    }
                }
            }
        }
    }

    @GetMapping
    public List<NoteResponseTo> getNotes() {
        return noteService.getNotes();
    }

    @GetMapping("/{id}")
    public NoteResponseTo getNote(@PathVariable Long id) {
        return noteService.getNoteById(id);
    }

    @DeleteMapping("/{id}")
    public NoteResponseTo deleteNote(@PathVariable Long id) {
        noteService.deleteNote(id);
        return new NoteResponseTo();
    }

    @PostMapping
    public NoteResponseTo saveNote(@RequestHeader("Accept-Language") String acceptLanguageHeader, @RequestBody NoteRequestTo note) {
        return noteService.saveNote(note, acceptLanguageHeader);
    }

    @PutMapping()
    public NoteResponseTo updateNote(@RequestHeader("Accept-Language") String acceptLanguageHeader, @RequestBody NoteRequestTo note) {
        return noteService.updateNote(note, acceptLanguageHeader);
    }

    @GetMapping("/byIssue/{id}")
    public List<NoteResponseTo> getEditorByIssueId(@PathVariable Long id) {
        return noteService.getNoteByIssueId(id);
    }
}
