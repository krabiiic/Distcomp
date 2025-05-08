package org.example.Mappers;

import org.example.Impl.DTO.MarkRequestTo;
import org.example.Impl.DTO.MarkResponseTo;
import org.example.Impl.Entity.Mark;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MarkMapper {
    Mark MarkResposeToToMark(MarkResponseTo markResponseTo);
    Mark MarkRequestToToMark(MarkRequestTo markRequestTo);
    MarkResponseTo MarkToMarkResponseTo(Mark mark);
    MarkRequestTo MarkToMarkRequestTo(Mark mark);
}
