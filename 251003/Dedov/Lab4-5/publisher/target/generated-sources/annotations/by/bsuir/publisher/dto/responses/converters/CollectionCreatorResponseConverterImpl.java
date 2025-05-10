package by.bsuir.publisher.dto.responses.converters;

import by.bsuir.publisher.domain.Editor;
import by.bsuir.publisher.dto.responses.EditorResponseDto;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-07T18:45:00+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.2 (Oracle Corporation)"
)
@Component
public class CollectionEditorResponseConverterImpl implements CollectionEditorResponseConverter {

    @Autowired
    private EditorResponseConverter editorResponseConverter;

    @Override
    public List<EditorResponseDto> toListDto(List<Editor> editors) {
        if ( editors == null ) {
            return null;
        }

        List<EditorResponseDto> list = new ArrayList<EditorResponseDto>( editors.size() );
        for ( Editor editor : editors ) {
            list.add( editorResponseConverter.toDto( editor ) );
        }

        return list;
    }
}
