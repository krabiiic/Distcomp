package by.bsuir.publisher.dto.requests.converters;

import by.bsuir.publisher.domain.Mark;
import by.bsuir.publisher.dto.requests.MarkRequestDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MarkRequestConverter {
    Mark fromDto(MarkRequestDto mark);
}
