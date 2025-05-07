package com.example.discussion.service.mapper;

import com.example.discussion.dto.MarkerRequestTo;
import com.example.discussion.dto.MarkerResponseTo;
import com.example.discussion.model.Marker;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MarkerMapper {

    Marker toEntity(MarkerRequestTo dto); // Преобразует DTO в сущность

    MarkerResponseTo toDto(Marker entity); // Преобразует сущность в DTO

    void updateEntityFromDto(MarkerRequestTo dto, @MappingTarget Marker entity); // Обновляет сущность на основе DTO
}