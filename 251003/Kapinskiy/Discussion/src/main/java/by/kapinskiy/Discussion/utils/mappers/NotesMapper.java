package by.kapinskiy.Discussion.utils.mappers;


import by.kapinskiy.Discussion.DTOs.Requests.NoteRequestDTO;
import by.kapinskiy.Discussion.DTOs.Responses.NoteResponseDTO;
import by.kapinskiy.Discussion.models.Note;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface NotesMapper {

    @Mapping(target = "id", expression = "java(note.getKey().getId())")
    @Mapping(target = "issueId", expression = "java(note.getKey().getIssueId())")
    NoteResponseDTO toNoteResponse(Note note);

    List<NoteResponseDTO> toNoteResponseList(Iterable<Note> notes);

    @Mapping(target = "key", expression = "java(new Note.NoteKey(noteRequestDTO.getIssueId()))")
    Note toNote(NoteRequestDTO noteRequestDTO);
}
