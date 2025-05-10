package com.example.rv1.mapper;

import com.example.rv1.dto.EditorDTO;
import com.example.rv1.entity.Editor;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EditorMapper {
    Editor toEditor(EditorDTO editorDTO);

    EditorDTO toEditorDTO(Editor editor);

    List<Editor> toEditorList(List<EditorDTO> editorDTOList);

    List<EditorDTO> toEditorDTOList(List<Editor> editors);
}
