package by.bsuir.publisher.dto.responses.converters;

import by.bsuir.publisher.domain.Mark;
import by.bsuir.publisher.dto.responses.MarkResponseDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = MarkResponseConverter.class)
public interface CollectionMarkResponseConverter {
    List<MarkResponseDto> toListDto(List<Mark> tags);
}
