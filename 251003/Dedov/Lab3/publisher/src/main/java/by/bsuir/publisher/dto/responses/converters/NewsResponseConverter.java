package by.bsuir.publisher.dto.responses.converters;

import by.bsuir.publisher.domain.News;
import by.bsuir.publisher.dto.responses.NewsResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NewsResponseConverter {
    @Mapping(source = "editor.id", target = "editorId")
    NewsResponseDto toDto(News news);
}
