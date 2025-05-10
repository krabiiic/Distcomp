package by.bsuir.publisher.dto.requests.converters;

import by.bsuir.publisher.domain.Mark;
import by.bsuir.publisher.dto.requests.MarkRequestDto;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-07T19:09:51+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.15 (Eclipse Adoptium)"
)
@Component
public class MarkRequestConverterImpl implements MarkRequestConverter {

    @Override
    public Mark fromDto(MarkRequestDto mark) {
        if ( mark == null ) {
            return null;
        }

        Mark.MarkBuilder<?, ?> mark1 = Mark.builder();

        mark1.id( mark.getId() );
        mark1.name( mark.getName() );

        return mark1.build();
    }
}
