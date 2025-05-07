package by.ustsinovich.distcomp.lab1.controller;

import by.ustsinovich.distcomp.lab1.dto.request.NoteRequestTo;
import by.ustsinovich.distcomp.lab1.dto.response.NoteResponseTo;
import by.ustsinovich.distcomp.lab1.dto.specification.filter.NoteFilterCriteria;
import by.ustsinovich.distcomp.lab1.dto.specification.pagination.PaginationCriteria;
import by.ustsinovich.distcomp.lab1.dto.specification.sort.NoteSortCriteria;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface NoteController {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    NoteResponseTo createNote(@RequestBody NoteRequestTo createNoteDto);

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    List<NoteResponseTo> findAllNotes(
            NoteFilterCriteria filterCriteria,
            List<NoteSortCriteria> sortCriteria,
            PaginationCriteria paginationCriteria
    );

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    NoteResponseTo findNoteById(@PathVariable Long id);

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    NoteResponseTo updateNoteById(@PathVariable Long id, @RequestBody NoteRequestTo updateNoteDto);

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    void deleteNoteById(@PathVariable Long id);

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{id}")
    NoteResponseTo patchNoteById(@PathVariable Long id, @RequestBody NoteRequestTo patchNoteDto);

}
