package by.bsuir.mapper;

import by.bsuir.dto.MarkRequestTo;
import by.bsuir.dto.MarkResponseTo;
import by.bsuir.entities.Mark;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MarkMapper {
    Mark markRequestToMark (MarkRequestTo markRequestTo);
    MarkResponseTo markToMarkResponse(Mark mark);
}

