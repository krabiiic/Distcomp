package com.example.discussion.service.mapper;

import com.example.discussion.dto.NoteRequestTo;
import com.example.discussion.dto.NoteResponseTo;
import com.example.discussion.model.Note;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface NoteMapper {
    Note toEntity(NoteRequestTo dto); // Преобразует DTO в сущность

    NoteResponseTo toDto(Note entity); // Преобразует сущность в DTO

    void updateEntityFromDto(NoteRequestTo dto, @MappingTarget Note entity);
}
