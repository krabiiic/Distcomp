package by.bsuir.publisher.dto.responses.converters;

import by.bsuir.publisher.domain.News;
import by.bsuir.publisher.dto.responses.NewsResponseDto;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-09T01:08:10+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.15 (Eclipse Adoptium)"
)
@Component
public class CollectionNewsResponseConverterImpl implements CollectionNewsResponseConverter {

    @Autowired
    private NewsResponseConverter newsResponseConverter;

    @Override
    public List<NewsResponseDto> toListDto(List<News> tags) {
        if ( tags == null ) {
            return null;
        }

        List<NewsResponseDto> list = new ArrayList<NewsResponseDto>( tags.size() );
        for ( News news : tags ) {
            list.add( newsResponseConverter.toDto( news ) );
        }

        return list;
    }
}
