package by.bsuir.publisher.dto.responses.converters;

import by.bsuir.publisher.domain.Editor;
import by.bsuir.publisher.dto.responses.EditorResponseDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = EditorResponseConverter.class)
public interface CollectionEditorResponseConverter {
    List<EditorResponseDto> toListDto(List<Editor> editors);
}