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
    date = "2025-05-09T00:57:59+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.2 (Oracle Corporation)"
)
@Component
public class CollectionMarkResponseConverterImpl implements CollectionMarkResponseConverter {

    @Autowired
    private MarkResponseConverter markResponseConverter;

    @Override
    public List<MarkResponseDto> toListDto(List<Mark> marks) {
        if ( marks == null ) {
            return null;
        }

        List<MarkResponseDto> list = new ArrayList<MarkResponseDto>( marks.size() );
        for ( Mark mark : marks ) {
            list.add( markResponseConverter.toDto( mark ) );
        }

        return list;
    }
}
