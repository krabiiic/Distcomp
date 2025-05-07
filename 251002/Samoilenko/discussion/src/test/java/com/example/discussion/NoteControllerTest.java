package com.example.discussion;

import com.example.discussion.controller.NoteController;
import com.example.discussion.dto.NoteRequestTo;
import com.example.discussion.dto.NoteResponseTo;
import com.example.discussion.service.NoteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(NoteController.class)
@AutoConfigureMockMvc
class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NoteService noteService;

    @Autowired
    private ObjectMapper objectMapper;

    private final String BASE_URL = "/api/v1.0/notes";
    private NoteResponseTo testNote;
    private NoteRequestTo validRequest;

    @BeforeEach
    void setUp() {
        testNote = new NoteResponseTo();
        testNote.setContent("Test content");
        testNote.setNewsId(1L);
        testNote.setId(1L);
        validRequest = new NoteRequestTo();
        validRequest.setContent("Valid content");
        validRequest.setNewsId(1l);
    }

    @Test
    void getAllNotes_ShouldReturn200() throws Exception {
        List<NoteResponseTo> notes = Collections.singletonList(testNote);
        Mockito.when(noteService.findAll()).thenReturn(notes);

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].content").value("Test content"))
                .andExpect(jsonPath("$[0].newsId").value(1L));
    }

    @Test
    void getNoteById_WhenExists_ShouldReturn200() throws Exception {
        Mockito.when(noteService.findById(1L)).thenReturn(testNote);

        mockMvc.perform(get(BASE_URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void getNoteById_WhenNotExists_ShouldReturn404() throws Exception {
        Mockito.when(noteService.findById(999L)).thenReturn(null);

        mockMvc.perform(get(BASE_URL + "/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createNote_WithValidData_ShouldReturn201() throws Exception {
        Mockito.when(noteService.save(any(NoteRequestTo.class))).thenReturn(testNote);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }


    @Test
    void updateNote_WhenExists_ShouldReturn200() throws Exception {
        NoteRequestTo updateRequest = new NoteRequestTo();
        updateRequest.setContent("Updated content");
        updateRequest.setNewsId(1L);
        NoteResponseTo updatedNote = new NoteResponseTo();
        updatedNote.setContent("Updated content");
        updatedNote.setNewsId(1L);

        Mockito.when(noteService.update(any(NoteRequestTo.class))).thenReturn(updatedNote);

        mockMvc.perform(put(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Updated content"));
    }

    @Test
    void updateNote_WhenNotExists_ShouldReturn404() throws Exception {
        NoteRequestTo updateRequest = new NoteRequestTo();
        updateRequest.setNewsId(1l);
        updateRequest.setContent("Content");
        Mockito.when(noteService.update(any(NoteRequestTo.class))).thenReturn(null);

        mockMvc.perform(put(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteNote_ShouldReturn204() throws Exception {
        Mockito.doNothing().when(noteService).deleteById(1L);

        mockMvc.perform(delete(BASE_URL + "/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(noteService).deleteById(1L);
    }

}