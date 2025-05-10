package by.bsuir.controllers;

import by.bsuir.dto.NoteRequestTo;
import by.bsuir.dto.NoteResponseTo;
import by.bsuir.exceptions.NotFoundException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import java.time.Duration;
import java.util.List;

@RestController
@RequestMapping("/api/v1.0/notes")
public class NoteController {
    @Autowired
    private RestClient restClient;
    @Autowired
    private KafkaConsumer<String, NoteResponseTo> kafkaConsumer;
    @Autowired
    private KafkaSender kafkaSender;
    private String inTopic = "InTopic";
    private String outTopic = "OutTopic";
    private String uriBase = "http://localhost:24130/api/v1.0/notes";

    @GetMapping
    public ResponseEntity<List<?>> getNotes() {
        //kafkaSender.sendCustomMessage();
        return ResponseEntity.status(200).body(restClient.get()
                .uri(uriBase)
                .retrieve()
                .body(List.class));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoteResponseTo> getNote(@PathVariable Long id) throws NotFoundException {
        NoteRequestTo noteRequestTo = new NoteRequestTo();
        noteRequestTo.setMethod("GET");
        noteRequestTo.setId(id);
        kafkaSender.sendCustomMessage(noteRequestTo, inTopic);

        return ResponseEntity.status(200).body(listenKafka());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable Long id) throws NotFoundException {
        NoteRequestTo noteRequestTo = new NoteRequestTo();
        noteRequestTo.setMethod("DELETE");
        noteRequestTo.setId(id);
        kafkaSender.sendCustomMessage(noteRequestTo, inTopic);
        listenKafka();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @PostMapping
    public ResponseEntity<NoteResponseTo> saveNote(@RequestHeader(value = "Accept-Language", defaultValue = "en") String acceptLanguageHeader, @RequestBody NoteRequestTo note) throws NotFoundException {
        note.setCountry(acceptLanguageHeader);
        note.setMethod("POST");
        kafkaSender.sendCustomMessage(note, inTopic);
        return ResponseEntity.status(201).body(listenKafka());
    }

    @PutMapping()
    public ResponseEntity<NoteResponseTo> updateNote(@RequestHeader(value = "Accept-Language", defaultValue = "en") String acceptLanguageHeader, @RequestBody NoteRequestTo note) throws NotFoundException {
        note.setCountry(acceptLanguageHeader);
        note.setMethod("PUT");
        kafkaSender.sendCustomMessage(note, inTopic);
        return ResponseEntity.status(200).body(listenKafka());
    }

    @GetMapping("/byIssue/{id}")
    public ResponseEntity<?> getEditorByIssueId(@RequestHeader HttpHeaders headers, @PathVariable Long id) {
        return restClient.get()
                .uri(uriBase + "/byIssue/" + id)
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .retrieve()
                .body(ResponseEntity.class);
    }

    private NoteResponseTo listenKafka() throws NotFoundException {
        ConsumerRecords<String, NoteResponseTo> records = kafkaConsumer.poll(Duration.ofMillis(50000));
        for (ConsumerRecord<String, NoteResponseTo> record : records) {

            String key = record.key();
            NoteResponseTo value = record.value();
            if (value == null) {
                throw new NotFoundException("Not found", 40400L);
            }
            long offset = record.offset();
            int partition = record.partition();
            System.out.println("Received message: key = " + key + ", value = " + value +
                    ", offset = " + offset + ", partition = " + partition);

            return value;
        }
        return null;
    }
}
