package by.kapinskiy.Publisher.utils.mappers;

import by.kapinskiy.Publisher.DTOs.Requests.NoteRequestDTO;
import by.kapinskiy.Publisher.DTOs.Responses.NoteResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface NotesMapper {
    @Mapping(target = "id", expression = "java(noteRequestDTO.getId())")
    @Mapping(target = "issueId", expression = "java(noteRequestDTO.getIssueId())")
    @Mapping(target = "content", expression = "java(noteRequestDTO.getContent())")
    NoteResponseDTO toNoteResponse(NoteRequestDTO noteRequestDTO);
}
