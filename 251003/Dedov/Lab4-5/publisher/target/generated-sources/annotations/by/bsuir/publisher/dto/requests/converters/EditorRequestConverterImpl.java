package by.bsuir.publisher.dto.requests.converters;

import by.bsuir.publisher.domain.Editor;
import by.bsuir.publisher.dto.requests.EditorRequestDto;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-09T01:08:10+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.15 (Eclipse Adoptium)"
)
@Component
public class EditorRequestConverterImpl implements EditorRequestConverter {

    @Override
    public Editor fromDto(EditorRequestDto editor) {
        if ( editor == null ) {
            return null;
        }

        Editor.EditorBuilder<?, ?> editor1 = Editor.builder();

        editor1.id( editor.getId() );
        editor1.login( editor.getLogin() );
        editor1.password( editor.getPassword() );
        editor1.firstname( editor.getFirstname() );
        editor1.lastname( editor.getLastname() );

        return editor1.build();
    }
}
