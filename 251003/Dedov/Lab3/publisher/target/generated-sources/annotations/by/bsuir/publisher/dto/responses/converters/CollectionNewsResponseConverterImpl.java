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
    date = "2025-05-09T00:57:59+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.2 (Oracle Corporation)"
)
@Component
public class CollectionNewsResponseConverterImpl implements CollectionNewsResponseConverter {

    @Autowired
    private NewsResponseConverter newsResponseConverter;

    @Override
    public List<NewsResponseDto> toListDto(List<News> marks) {
        if ( marks == null ) {
            return null;
        }

        List<NewsResponseDto> list = new ArrayList<NewsResponseDto>( marks.size() );
        for ( News news : marks ) {
            list.add( newsResponseConverter.toDto( news ) );
        }

        return list;
    }
}
