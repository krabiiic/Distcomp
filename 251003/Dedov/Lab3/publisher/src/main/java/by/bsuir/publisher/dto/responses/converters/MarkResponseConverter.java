package by.bsuir.publisher.dto.responses.converters;

import by.bsuir.publisher.domain.Mark;
import by.bsuir.publisher.dto.responses.MarkResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MarkResponseConverter {
    MarkResponseDto toDto(Mark mark);
}
