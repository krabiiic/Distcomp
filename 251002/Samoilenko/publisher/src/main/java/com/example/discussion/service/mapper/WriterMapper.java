package com.example.discussion.service.mapper;

import com.example.discussion.dto.WriterRequestTo;
import com.example.discussion.dto.WriterResponseTo;
import com.example.discussion.model.Writer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface WriterMapper {
    @Mapping(source = "firstname", target = "firstname")
    @Mapping(source = "lastname", target = "lastname")
    @Mapping(target = "id", ignore = true)  // Игнорируем ID при создании
    Writer toEntity(WriterRequestTo dto);

    @Mapping(source = "firstname", target = "firstname")
    @Mapping(source = "lastname", target = "lastname")
    WriterResponseTo toDto(Writer writer);

    @Mapping(target = "id", ignore = true)  // Игнорируем ID при обновлении
    void updateEntityFromDto(WriterRequestTo dto, @MappingTarget Writer entity);
}