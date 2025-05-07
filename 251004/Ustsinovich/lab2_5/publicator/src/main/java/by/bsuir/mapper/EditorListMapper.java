package by.bsuir.mapper;

import by.bsuir.dto.EditorRequestTo;
import by.bsuir.dto.EditorResponseTo;
import by.bsuir.entities.Editor;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = EditorMapper.class)
public interface EditorListMapper {
    List<Editor> toEditorList(List<EditorRequestTo> editors);
    List<EditorResponseTo> toEditorResponseList(List<Editor> editors);
}
