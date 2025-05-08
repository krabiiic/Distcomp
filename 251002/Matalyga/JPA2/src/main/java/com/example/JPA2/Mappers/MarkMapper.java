package com.example.JPA2.Mappers;

import com.example.JPA2.Impl.DTO.MarkRequestTo;
import com.example.JPA2.Impl.DTO.MarkResponseTo;
import com.example.JPA2.Impl.Entity.Mark;
import org.mapstruct.Mapping;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MarkMapper {
    Mark MarkResposeToToMark(MarkResponseTo markResponseTo);
    Mark MarkRequestToToMark(MarkRequestTo markRequestTo);
    MarkResponseTo MarkToMarkResponseTo(Mark mark);
    MarkRequestTo MarkToMarkRequestTo(Mark mark);
}
