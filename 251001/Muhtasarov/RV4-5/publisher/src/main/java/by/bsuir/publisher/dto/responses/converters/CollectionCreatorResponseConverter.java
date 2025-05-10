package by.bsuir.publisher.dto.responses.converters;

import by.bsuir.publisher.domain.Creator;
import by.bsuir.publisher.dto.responses.CreatorResponseDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = CreatorResponseConverter.class)
public interface CollectionCreatorResponseConverter {
    List<CreatorResponseDto> toListDto(List<Creator> creators);
}