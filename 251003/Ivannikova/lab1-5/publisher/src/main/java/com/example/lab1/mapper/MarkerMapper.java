package com.example.lab1.mapper;

import com.example.lab1.dto.MarkerRequestTo;
import com.example.lab1.dto.MarkerResponseTo;
import com.example.lab1.model.Marker;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MarkerMapper {
    Marker toEntity(MarkerRequestTo dto);
    MarkerResponseTo toDto(Marker entity);
}
