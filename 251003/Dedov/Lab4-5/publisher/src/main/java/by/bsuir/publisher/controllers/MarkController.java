package by.bsuir.publisher.controllers;

import by.bsuir.publisher.dto.requests.MarkRequestDto;
import by.bsuir.publisher.dto.responses.MarkResponseDto;
import by.bsuir.publisher.exceptions.EntityExistsException;
import by.bsuir.publisher.exceptions.Messages;
import by.bsuir.publisher.exceptions.NoEntityExistsException;
import by.bsuir.publisher.services.MarkService;
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
@RequestMapping("/marks")
@RequiredArgsConstructor
public class MarkController {
    private final MarkService markService;

    @PostMapping
    public ResponseEntity<MarkResponseDto> create(@RequestBody MarkRequestDto mark) throws EntityExistsException {
        return ResponseEntity.status(HttpStatus.CREATED).body(markService.create(mark));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MarkResponseDto> read(@PathVariable("id") Long id) throws NoEntityExistsException {
        return ResponseEntity.status(HttpStatus.OK).body(markService.read(id).orElseThrow(() ->
                new NoEntityExistsException(Messages.NoEntityExistsException)));
    }

    @GetMapping
    public ResponseEntity<List<MarkResponseDto>> read() {
        return ResponseEntity.status(HttpStatus.OK).body(markService.readAll());
    }

    @PutMapping
    public ResponseEntity<MarkResponseDto> update(@RequestBody MarkRequestDto mark) throws NoEntityExistsException {
        return ResponseEntity.status(HttpStatus.OK).body(markService.update(mark));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> delete(@PathVariable("id") Long id) throws NoEntityExistsException {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(markService.delete(id));
    }
}
