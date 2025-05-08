package by.bsuir.publisher.dto.requests.converters;

import by.bsuir.publisher.domain.Editor;
import by.bsuir.publisher.domain.News;
import by.bsuir.publisher.dto.requests.NewsRequestDto;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-09T01:08:10+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.15 (Eclipse Adoptium)"
)
@Component
public class NewsRequestConverterImpl implements NewsRequestConverter {

    @Override
    public News fromDto(NewsRequestDto tweet) {
        if ( tweet == null ) {
            return null;
        }

        News.NewsBuilder<?, ?> news = News.builder();

        news.editor( newsRequestDtoToEditor( tweet ) );
        news.id( tweet.getId() );
        news.title( tweet.getTitle() );
        news.content( tweet.getContent() );

        return news.build();
    }

    protected Editor newsRequestDtoToEditor(NewsRequestDto newsRequestDto) {
        if ( newsRequestDto == null ) {
            return null;
        }

        Editor.EditorBuilder<?, ?> editor = Editor.builder();

        editor.id( newsRequestDto.getEditorId() );

        return editor.build();
    }
}
