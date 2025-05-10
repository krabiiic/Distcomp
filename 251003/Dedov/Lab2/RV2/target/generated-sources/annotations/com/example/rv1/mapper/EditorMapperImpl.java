package com.example.rv1.mapper;

import com.example.rv1.dto.EditorDTO;
import com.example.rv1.entity.Editor;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-02T18:43:27+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.2 (Oracle Corporation)"
)
@Component
public class EditorMapperImpl implements EditorMapper {

    @Override
    public Editor toEditor(EditorDTO editorDTO) {
        if ( editorDTO == null ) {
            return null;
        }

        Editor editor = new Editor();

        editor.setId( editorDTO.getId() );
        editor.setLogin( editorDTO.getLogin() );
        editor.setPassword( editorDTO.getPassword() );
        editor.setFirstName( editorDTO.getFirstName() );
        editor.setLastName( editorDTO.getLastName() );

        return editor;
    }

    @Override
    public EditorDTO toEditorDTO(Editor editor) {
        if ( editor == null ) {
            return null;
        }

        EditorDTO editorDTO = new EditorDTO();

        editorDTO.setId( editor.getId() );
        editorDTO.setLogin( editor.getLogin() );
        editorDTO.setPassword( editor.getPassword() );
        editorDTO.setFirstName( editor.getFirstName() );
        editorDTO.setLastName( editor.getLastName() );

        return editorDTO;
    }

    @Override
    public List<Editor> toEditorList(List<EditorDTO> editorDTOList) {
        if ( editorDTOList == null ) {
            return null;
        }

        List<Editor> list = new ArrayList<Editor>( editorDTOList.size() );
        for ( EditorDTO editorDTO : editorDTOList ) {
            list.add( toEditor( editorDTO ) );
        }

        return list;
    }

    @Override
    public List<EditorDTO> toEditorDTOList(List<Editor> editors) {
        if ( editors == null ) {
            return null;
        }

        List<EditorDTO> list = new ArrayList<EditorDTO>( editors.size() );
        for ( Editor editor : editors ) {
            list.add( toEditorDTO( editor ) );
        }

        return list;
    }
}
