package com.example.discussion;

import com.example.discussion.dto.NoteRequestTo;
import com.example.discussion.dto.NoteResponseTo;
import com.example.discussion.model.Note;
import com.example.discussion.repository.NoteRepository;
import com.example.discussion.service.NoteService;
import com.example.discussion.service.SequenceGeneratorService;
import com.example.discussion.service.mapper.NoteMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NoteServiceTest {

    private NoteRepository noteRepository;
    private SequenceGeneratorService sequenceGeneratorService;
    private NoteMapper noteMapper;
    private NoteService noteService;

    @BeforeEach
    void setUp() {
        noteRepository = mock(NoteRepository.class);
        sequenceGeneratorService = mock(SequenceGeneratorService.class);
        noteMapper = Mappers.getMapper(NoteMapper.class);

        noteService = new NoteService(noteRepository, sequenceGeneratorService);
    }

    @Test
    void findAll_ShouldReturnNoteResponseToList() {
        Note note = new Note("Test content", 1L);
        when(noteRepository.findAll()).thenReturn(Collections.singletonList(note));

        List<NoteResponseTo> result = noteService.findAll();

        assertEquals(1, result.size());
        assertEquals("Test content", result.get(0).getContent());
    }

    @Test
    void findById_WhenNoteExists_ShouldReturnNoteResponseTo() {
        Note note = new Note("Test content", 1L);
        when(noteRepository.findById(1L)).thenReturn(Optional.of(note));

        NoteResponseTo result = noteService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getNewsId());
        assertEquals("Test content", result.getContent());
    }

    @Test
    void findById_WhenNoteNotFound_ShouldReturnNull() {
        when(noteRepository.findById(1L)).thenReturn(Optional.empty());

        NoteResponseTo result = noteService.findById(1L);

        assertNull(result);
    }

    @Test
    void save_ShouldReturnSavedNoteResponseTo() {
        NoteRequestTo request = new NoteRequestTo();
        request.setContent("New Note");
        request.setNewsId(1L);

        Note note = noteMapper.toEntity(request);
        note.setId(100L);

        when(sequenceGeneratorService.generateSequence("note_sequence")).thenReturn(100L);
        when(noteRepository.save(any(Note.class))).thenAnswer(invocation -> invocation.getArgument(0));

        NoteResponseTo result = noteService.save(request);

        assertNotNull(result);
        assertEquals("New Note", result.getContent());
        assertEquals(1L, result.getNewsId());
        assertEquals(100L, result.getId());
    }

    @Test
    void update_WhenNoteExists_ShouldReturnUpdatedNoteResponseTo() {
        Note existingNote = new Note("Old content", 1L);
        NoteRequestTo request = new NoteRequestTo();
        request.setId(1L);
        request.setContent("Updated content");
        request.setNewsId(1L);

        when(noteRepository.findById(1L)).thenReturn(Optional.of(existingNote));
        when(noteRepository.save(any(Note.class))).thenAnswer(invocation -> invocation.getArgument(0));

        NoteResponseTo result = noteService.update(request);

        assertEquals("Updated content", result.getContent());
    }

    @Test
    void update_WhenNoteNotFound_ShouldThrowException() {
        NoteRequestTo request = new NoteRequestTo();
        request.setId(99L);
        request.setContent("Doesn't matter");
        request.setNewsId(1L);

        when(noteRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> noteService.update(request));
        assertEquals("Note not found", ex.getMessage());
    }

    @Test
    void deleteById_ShouldCallRepository() {
        doNothing().when(noteRepository).deleteById(1L);

        noteService.deleteById(1L);

        verify(noteRepository).deleteById(1L);
    }
}
