package by.bsuir.publisher.dto.responses.converters;

import by.bsuir.publisher.domain.Mark;
import by.bsuir.publisher.dto.responses.MarkResponseDto;
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
public class CollectionMarkResponseConverterImpl implements CollectionMarkResponseConverter {

    @Autowired
    private MarkResponseConverter markResponseConverter;

    @Override
    public List<MarkResponseDto> toListDto(List<Mark> tags) {
        if ( tags == null ) {
            return null;
        }

        List<MarkResponseDto> list = new ArrayList<MarkResponseDto>( tags.size() );
        for ( Mark mark : tags ) {
            list.add( markResponseConverter.toDto( mark ) );
        }

        return list;
    }
}
