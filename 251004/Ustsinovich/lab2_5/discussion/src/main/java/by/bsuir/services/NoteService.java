package by.bsuir.services;

import by.bsuir.dto.NoteRequestTo;
import by.bsuir.dto.NoteResponseTo;
import by.bsuir.entities.Note;
import by.bsuir.entities.NoteKey;
import by.bsuir.exceptions.DeleteException;
import by.bsuir.exceptions.NotFoundException;
import by.bsuir.exceptions.UpdateException;
import by.bsuir.repository.NoteRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalTime;
import java.util.*;

@Service
@Validated
public class NoteService {
    @Autowired
    NoteRepository noteDao;

    public NoteResponseTo getNoteById(Long id) throws NotFoundException {
        Optional<Note> note = noteDao.findById(id).stream().findFirst();
        return note.map(this::noteToNoteResponse).orElseThrow(() -> new NotFoundException("Note not found!", 40004L));
    }

    public List<NoteResponseTo> getNotes() {
        return toNoteResponseList(noteDao.findAll());
    }

    public NoteResponseTo saveNote(@Valid NoteRequestTo note, String country) {
        Note noteToSave = noteRequestToNote(note);
        noteToSave.setId(getId());
        noteToSave.setCountry(getCountry(country));
        return noteToNoteResponse(noteDao.save(noteToSave));
    }

    public void deleteNote(Long id) throws DeleteException {
        Optional<Note> note = noteDao.findById(id).stream().findFirst();
        if (note.isEmpty()) {
            throw new DeleteException("Note not found!", 40004L);
        } else {
            noteDao.deleteByCountryAndIssueIdAndId(note.get().getCountry(), note.get().getIssueId(), note.get().getId());
        }
    }

    public NoteResponseTo updateNote(@Valid NoteRequestTo note, String country) throws UpdateException {
        Note noteToUpdate = noteRequestToNote(note);
        noteToUpdate.setId(note.getId());
        noteToUpdate.setCountry(getCountry(country));
        Optional<Note> noteOptional = noteDao.findById(note.getId()).stream().findFirst();
        if (noteOptional.isEmpty()) {
            throw new UpdateException("Note not found!", 40004L);
        } else {
            noteDao.deleteByCountryAndIssueIdAndId(noteOptional.get().getCountry(), noteOptional.get().getIssueId(), noteOptional.get().getId());
            return noteToNoteResponse(noteDao.save(noteToUpdate));
        }
    }

    public List<NoteResponseTo> getNoteByIssueId(Long issueId) throws NotFoundException {
        List<Note> note = noteDao.findByIssueId(issueId);
        if (note.isEmpty()) {
            throw new NotFoundException("Note not found!", 40004L);
        }
        return toNoteResponseList(note);
    }

    private NoteResponseTo noteToNoteResponse(Note note) {
        NoteResponseTo noteResponseTo = new NoteResponseTo();
        noteResponseTo.setIssueId(note.getIssueId());
        noteResponseTo.setId(note.getId());
        noteResponseTo.setContent(note.getContent());
        return noteResponseTo;
    }

    private List<NoteResponseTo> toNoteResponseList(List<Note> notes) {
        List<NoteResponseTo> response = new ArrayList<>();
        for (Note note : notes) {
            response.add(noteToNoteResponse(note));
        }
        return response;
    }

    private Note noteRequestToNote(NoteRequestTo requestTo) {
        Note note = new Note();
        note.setId(requestTo.getId());
        note.setContent(requestTo.getContent());
        note.setIssueId(requestTo.getIssueId());
        return note;
    }

    private long getId (){
        int currentSecond = (int) (System.currentTimeMillis() / 1000);

        int shiftedTime = currentSecond << 10;

        int randomBits = new Random().nextInt(1 << 10);

        return Math.abs(shiftedTime | randomBits);
    }

    private String getCountry(String requestHeader){
        Map<String, Double> languageMap = getStringDoubleMap(requestHeader);
        Map<String, Double> loadMap = new HashMap<>();
        for (String country: languageMap.keySet()){
            loadMap.put(country, noteDao.countByCountry(country)*(1-languageMap.get(country)));
        }
        double minValue = Double.MAX_VALUE;
        String minKey = null;

        for (Map.Entry<String, Double> entry : loadMap.entrySet()) {
            if (entry.getValue() < minValue) {
                minValue = entry.getValue();
                minKey = entry.getKey();
            }
        }
        return minKey;
    }

    private static Map<String, Double> getStringDoubleMap(String requestHeader) {
        String[] languages = requestHeader.split(",");
        Map<String, Double> languageMap = new HashMap<>();
        for (String language : languages) {
            String[] parts = language.split(";");
            String lang = parts[0].trim();
            double priority = 1.0; // По умолчанию
            if (parts.length > 1) {
                String[] priorityParts = parts[1].split("=");
                priority = Double.parseDouble(priorityParts[1]);
            }
            languageMap.put(lang, priority);
        }
        return languageMap;
    }
}
