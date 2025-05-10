package by.bsuir.publisher.dto.responses.converters;

import by.bsuir.publisher.domain.Editor;
import by.bsuir.publisher.dto.responses.EditorResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EditorResponseConverter {
    EditorResponseDto toDto(Editor editor);
}
