package by.bsuir.publisher.dto.responses.converters;

import by.bsuir.publisher.domain.Editor;
import by.bsuir.publisher.dto.responses.EditorResponseDto;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-09T00:57:59+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.2 (Oracle Corporation)"
)
@Component
public class EditorResponseConverterImpl implements EditorResponseConverter {

    @Override
    public EditorResponseDto toDto(Editor editor) {
        if ( editor == null ) {
            return null;
        }

        EditorResponseDto.EditorResponseDtoBuilder<?, ?> editorResponseDto = EditorResponseDto.builder();

        editorResponseDto.id( editor.getId() );
        editorResponseDto.login( editor.getLogin() );
        editorResponseDto.password( editor.getPassword() );
        editorResponseDto.firstname( editor.getFirstname() );
        editorResponseDto.lastname( editor.getLastname() );

        return editorResponseDto.build();
    }
}
