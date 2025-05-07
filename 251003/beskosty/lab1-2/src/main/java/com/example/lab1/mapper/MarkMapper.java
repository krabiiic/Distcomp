package com.example.lab1.mapper;

import com.example.lab1.dto.MarkRequestTo;
import com.example.lab1.dto.MarkResponseTo;
import com.example.lab1.model.Mark;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MarkMapper {
    Mark toEntity(MarkRequestTo dto);
    MarkResponseTo toDto(Mark entity);
}
