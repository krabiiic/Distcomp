package by.ustsinovich.distcomp.lab1.service;

import by.ustsinovich.distcomp.lab1.dto.request.EditorRequestTo;
import by.ustsinovich.distcomp.lab1.dto.response.EditorResponseTo;
import by.ustsinovich.distcomp.lab1.dto.specification.filter.EditorFilterCriteria;
import by.ustsinovich.distcomp.lab1.dto.specification.pagination.PaginationCriteria;
import by.ustsinovich.distcomp.lab1.dto.specification.sort.EditorSortCriteria;

import java.util.List;

public interface EditorService {

    EditorResponseTo createEditor(EditorRequestTo createEditorDto);

    List<EditorResponseTo> findAllEditors(
            EditorFilterCriteria filterCriteria,
            List<EditorSortCriteria> sortCriteria,
            PaginationCriteria paginationCriteria
    );

    EditorResponseTo findEditorById(Long id);

    EditorResponseTo updateEditorById(Long id, EditorRequestTo updateEditorDto);

    void deleteEditorById(Long id);

    EditorResponseTo patchEditorById(Long id, EditorRequestTo patchEditorDto);

}
