package by.bsuir.mapper;

import by.bsuir.dto.EditorRequestTo;
import by.bsuir.dto.EditorResponseTo;
import by.bsuir.entities.Editor;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EditorMapper {
    Editor editorRequestToEditor(EditorRequestTo editorRequestTo);

    EditorResponseTo editorToEditorResponse(Editor editor);
}

