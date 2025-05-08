package by.bsuir.publisher.dto.requests.converters;

import by.bsuir.publisher.domain.Editor;
import by.bsuir.publisher.dto.requests.EditorRequestDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EditorRequestConverter {
    Editor fromDto(EditorRequestDto editor);
}
