package by.bsuir.publisher.dto.responses.converters;

import by.bsuir.publisher.domain.Editor;
import by.bsuir.publisher.domain.News;
import by.bsuir.publisher.dto.responses.NewsResponseDto;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-09T01:08:10+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.15 (Eclipse Adoptium)"
)
@Component
public class NewsResponseConverterImpl implements NewsResponseConverter {

    @Override
    public NewsResponseDto toDto(News news) {
        if ( news == null ) {
            return null;
        }

        NewsResponseDto.NewsResponseDtoBuilder<?, ?> newsResponseDto = NewsResponseDto.builder();

        newsResponseDto.editorId( newsEditorId( news ) );
        newsResponseDto.id( news.getId() );
        newsResponseDto.title( news.getTitle() );
        newsResponseDto.content( news.getContent() );
        newsResponseDto.created( news.getCreated() );
        newsResponseDto.modified( news.getModified() );

        return newsResponseDto.build();
    }

    private Long newsEditorId(News news) {
        if ( news == null ) {
            return null;
        }
        Editor editor = news.getEditor();
        if ( editor == null ) {
            return null;
        }
        Long id = editor.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
