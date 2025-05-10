package by.bsuir.publisher.dto.responses.converters;

import by.bsuir.publisher.domain.Creator;
import by.bsuir.publisher.dto.responses.CreatorResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CreatorResponseConverter {
    CreatorResponseDto toDto(Creator creator);
}
