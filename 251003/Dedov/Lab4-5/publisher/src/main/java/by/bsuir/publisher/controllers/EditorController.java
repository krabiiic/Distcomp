package by.bsuir.publisher.controllers;

import by.bsuir.publisher.dto.requests.EditorRequestDto;
import by.bsuir.publisher.dto.responses.EditorResponseDto;
import by.bsuir.publisher.exceptions.EntityExistsException;
import by.bsuir.publisher.exceptions.Messages;
import by.bsuir.publisher.exceptions.NoEntityExistsException;
import by.bsuir.publisher.services.EditorService;
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
@RequestMapping("/editors")
@RequiredArgsConstructor
public class EditorController {
    private final EditorService editorService;

    @PostMapping
    public ResponseEntity<EditorResponseDto> create(@RequestBody EditorRequestDto editor) throws EntityExistsException {
        return ResponseEntity.status(HttpStatus.CREATED).body(editorService.create(editor));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EditorResponseDto> read(@PathVariable("id") Long id) throws NoEntityExistsException {
        return ResponseEntity.status(HttpStatus.OK).body(editorService.read(id).orElseThrow(() ->
                new NoEntityExistsException(Messages.NoEntityExistsException)));
    }

    @GetMapping
    public ResponseEntity<List<EditorResponseDto>> read() {
        return ResponseEntity.status(HttpStatus.OK).body(editorService.readAll());
    }

    @PutMapping
    public ResponseEntity<EditorResponseDto> update(@RequestBody EditorRequestDto editor) throws NoEntityExistsException {
        return ResponseEntity.status(HttpStatus.OK).body(editorService.update(editor));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> delete(@PathVariable("id") Long id) throws NoEntityExistsException {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(editorService.delete(id));
    }
}
