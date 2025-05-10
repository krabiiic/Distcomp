package by.bsuir.publisher.controllers;

import by.bsuir.publisher.dto.requests.TopicRequestDto;
import by.bsuir.publisher.dto.responses.TopicResponseDto;
import by.bsuir.publisher.exceptions.EntityExistsException;
import by.bsuir.publisher.exceptions.Messages;
import by.bsuir.publisher.exceptions.NoEntityExistsException;
import by.bsuir.publisher.services.TopicService;
import lombok.RequiredArgsConstructor;
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

import java.util.List;

@RestController
@RequestMapping("/topics")
@RequiredArgsConstructor
public class TopicController {
    private final TopicService topicService;

    @PostMapping
    public ResponseEntity<TopicResponseDto> create(@RequestBody TopicRequestDto topic) throws EntityExistsException {
        return ResponseEntity.status(HttpStatus.CREATED).body(topicService.create(topic));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TopicResponseDto> read(@PathVariable("id") Long id) throws NoEntityExistsException {
        return ResponseEntity.status(HttpStatus.OK).body(topicService.read(id).orElseThrow(() ->
                new NoEntityExistsException(Messages.NoEntityExistsException)));
    }

    @GetMapping
    public ResponseEntity<List<TopicResponseDto>> read() {
        return ResponseEntity.status(HttpStatus.OK).body(topicService.readAll());
    }

    @PutMapping
    public ResponseEntity<TopicResponseDto> update(@RequestBody TopicRequestDto topic) throws NoEntityExistsException {
        return ResponseEntity.status(HttpStatus.OK).body(topicService.update(topic));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> delete(@PathVariable("id") Long id) throws NoEntityExistsException {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(topicService.delete(id));
    }
}
