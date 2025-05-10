package by.bsuir.publisher.dto.responses.converters;

import by.bsuir.publisher.domain.Mark;
import by.bsuir.publisher.dto.responses.MarkResponseDto;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-09T00:57:59+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.2 (Oracle Corporation)"
)
@Component
public class MarkResponseConverterImpl implements MarkResponseConverter {

    @Override
    public MarkResponseDto toDto(Mark mark) {
        if ( mark == null ) {
            return null;
        }

        MarkResponseDto.MarkResponseDtoBuilder<?, ?> markResponseDto = MarkResponseDto.builder();

        markResponseDto.id( mark.getId() );
        markResponseDto.name( mark.getName() );

        return markResponseDto.build();
    }
}
