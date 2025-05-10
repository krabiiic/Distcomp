package by.ustsinovich.distcomp.lab1.service.impl;

import by.ustsinovich.distcomp.lab1.dto.request.EditorRequestTo;
import by.ustsinovich.distcomp.lab1.dto.response.EditorResponseTo;
import by.ustsinovich.distcomp.lab1.dto.specification.filter.EditorFilterCriteria;
import by.ustsinovich.distcomp.lab1.dto.specification.pagination.PaginationCriteria;
import by.ustsinovich.distcomp.lab1.dto.specification.sort.EditorSortCriteria;
import by.ustsinovich.distcomp.lab1.service.EditorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class EditorServiceImpl implements EditorService {


    @Override
    public EditorResponseTo createEditor(EditorRequestTo createEditorDto) {
        return null;
    }

    @Override
    public List<EditorResponseTo> findAllEditors(
            EditorFilterCriteria filterCriteria,
            List<EditorSortCriteria> sortCriteria,
            PaginationCriteria paginationCriteria
    ) {
        return List.of();
    }

    @Override
    public EditorResponseTo findEditorById(Long id) {
        return null;
    }

    @Override
    public EditorResponseTo updateEditorById(Long id, EditorRequestTo updateEditorDto) {
        return null;
    }

    @Override
    public void deleteEditorById(Long id) {

    }

    @Override
    public EditorResponseTo patchEditorById(Long id, EditorRequestTo patchEditorDto) {
        return null;
    }

}