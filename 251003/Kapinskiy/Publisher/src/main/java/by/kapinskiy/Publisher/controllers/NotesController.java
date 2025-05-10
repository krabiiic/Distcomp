package by.kapinskiy.Publisher.controllers;

import by.kapinskiy.Publisher.DTOs.Requests.NoteRequestDTO;
import by.kapinskiy.Publisher.DTOs.Responses.NoteResponseDTO;
import by.kapinskiy.Publisher.services.NotesService;
import by.kapinskiy.Publisher.utils.NoteValidator;
import by.kapinskiy.Publisher.utils.exceptions.CustomInformativeException;
import by.kapinskiy.Publisher.utils.exceptions.NotFoundException;
import by.kapinskiy.Publisher.utils.exceptions.ValidationException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/notes")
@RequiredArgsConstructor
public class NotesController {
    private final NotesService notesService;

    private final NoteValidator noteValidator;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NoteResponseDTO createNote(@RequestBody @Valid NoteRequestDTO noteRequestDTO,
                                      BindingResult bindingResult) {
        validateRequest(noteRequestDTO, bindingResult);
        return notesService.createNote(noteRequestDTO);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public NoteResponseDTO getNoteById(@PathVariable Long id) {
        return notesService.getNoteById(id);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<NoteResponseDTO> getAllNotes() {
        return notesService.getAllNotes();
    }

    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public NoteResponseDTO updateNote(
                                      @RequestBody @Valid NoteRequestDTO noteRequestDTO) {
        return notesService.processNoteRequest("PUT", noteRequestDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteNote(@PathVariable Long id) {
        NoteRequestDTO request = new NoteRequestDTO();
        request.setId(id);
        notesService.processNoteRequest("DELETE", request);
    }

    private void validateRequest(NoteRequestDTO request, BindingResult bindingResult) {
        if (!bindingResult.hasFieldErrors()) {
            noteValidator.validate(request, bindingResult);
        }
        if (bindingResult.hasFieldErrors()) {
            throw new ValidationException(bindingResult);
        }
    }
}