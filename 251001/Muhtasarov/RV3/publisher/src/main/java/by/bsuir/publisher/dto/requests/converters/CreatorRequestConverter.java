package by.bsuir.publisher.dto.requests.converters;

import by.bsuir.publisher.domain.Creator;
import by.bsuir.publisher.dto.requests.CreatorRequestDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CreatorRequestConverter {
    Creator fromDto(CreatorRequestDto creator);
}
