package by.bsuir.publisher.controllers;

import by.bsuir.publisher.dto.requests.CreatorRequestDto;
import by.bsuir.publisher.dto.responses.CreatorResponseDto;
import by.bsuir.publisher.exceptions.EntityExistsException;
import by.bsuir.publisher.exceptions.Messages;
import by.bsuir.publisher.exceptions.NoEntityExistsException;
import by.bsuir.publisher.services.CreatorService;
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
@RequestMapping("/creators")
@RequiredArgsConstructor
public class CreatorController {
    private final CreatorService creatorService;

    @PostMapping
    public ResponseEntity<CreatorResponseDto> create(@RequestBody CreatorRequestDto creator) throws EntityExistsException {
        return ResponseEntity.status(HttpStatus.CREATED).body(creatorService.create(creator));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CreatorResponseDto> read(@PathVariable("id") Long id) throws NoEntityExistsException {
        return ResponseEntity.status(HttpStatus.OK).body(creatorService.read(id).orElseThrow(() ->
                new NoEntityExistsException(Messages.NoEntityExistsException)));
    }

    @GetMapping
    public ResponseEntity<List<CreatorResponseDto>> read() {
        return ResponseEntity.status(HttpStatus.OK).body(creatorService.readAll());
    }

    @PutMapping
    public ResponseEntity<CreatorResponseDto> update(@RequestBody CreatorRequestDto creator) throws NoEntityExistsException {
        return ResponseEntity.status(HttpStatus.OK).body(creatorService.update(creator));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> delete(@PathVariable("id") Long id) throws NoEntityExistsException {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(creatorService.delete(id));
    }
}
