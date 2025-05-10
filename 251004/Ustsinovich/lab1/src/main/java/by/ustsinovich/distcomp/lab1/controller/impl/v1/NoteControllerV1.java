package by.ustsinovich.distcomp.lab1.controller.impl.v1;

import by.ustsinovich.distcomp.lab1.controller.NoteController;
import by.ustsinovich.distcomp.lab1.dto.request.NoteRequestTo;
import by.ustsinovich.distcomp.lab1.dto.response.NoteResponseTo;
import by.ustsinovich.distcomp.lab1.dto.specification.filter.NoteFilterCriteria;
import by.ustsinovich.distcomp.lab1.dto.specification.pagination.PaginationCriteria;
import by.ustsinovich.distcomp.lab1.dto.specification.sort.NoteSortCriteria;
import by.ustsinovich.distcomp.lab1.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1.0/notes")
@RestController
public class NoteControllerV1 implements NoteController {

    private final NoteService noteService;

    @Override
    public NoteResponseTo createNote(NoteRequestTo createNoteDto) {
        return null;
    }

    @Override
    public List<NoteResponseTo> findAllNotes(
            NoteFilterCriteria filterCriteria,
            List<NoteSortCriteria> sortCriteria,
            PaginationCriteria paginationCriteria
    ) {
        return List.of();
    }

    @Override
    public NoteResponseTo findNoteById(Long id) {
        return null;
    }

    @Override
    public NoteResponseTo updateNoteById(Long id, NoteRequestTo updateNoteDto) {
        return null;
    }

    @Override
    public void deleteNoteById(Long id) {

    }

    @Override
    public NoteResponseTo patchNoteById(Long id, NoteRequestTo patchNoteDto) {
        return null;
    }

}
