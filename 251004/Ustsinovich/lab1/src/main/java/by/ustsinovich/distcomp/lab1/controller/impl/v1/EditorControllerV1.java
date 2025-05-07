package by.ustsinovich.distcomp.lab1.controller.impl.v1;

import by.ustsinovich.distcomp.lab1.controller.EditorController;
import by.ustsinovich.distcomp.lab1.dto.request.EditorRequestTo;
import by.ustsinovich.distcomp.lab1.dto.response.EditorResponseTo;
import by.ustsinovich.distcomp.lab1.dto.specification.filter.EditorFilterCriteria;
import by.ustsinovich.distcomp.lab1.dto.specification.pagination.PaginationCriteria;
import by.ustsinovich.distcomp.lab1.dto.specification.sort.EditorSortCriteria;
import by.ustsinovich.distcomp.lab1.service.EditorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1.0/editors")
@RestController
public class EditorControllerV1 implements EditorController {

    private final EditorService editorService;

    @Override
    public EditorResponseTo createEditor(EditorRequestTo createEditorDto) {
        return editorService.createEditor(createEditorDto);
    }

    @Override
    public List<EditorResponseTo> findAllEditors(
            EditorFilterCriteria filterCriteria,
            List<EditorSortCriteria> sortCriteria,
            PaginationCriteria paginationCriteria
    ) {
        return editorService.findAllEditors(filterCriteria, sortCriteria, paginationCriteria);
    }

    @Override
    public EditorResponseTo findEditorById(Long id) {
        return editorService.findEditorById(id);
    }

    @Override
    public EditorResponseTo updateEditorById(Long id, EditorRequestTo updateEditorDto) {
        return editorService.updateEditorById(id, updateEditorDto);
    }

    @Override
    public void deleteEditorById(Long id) {
        editorService.deleteEditorById(id);
    }

    @Override
    public EditorResponseTo patchEditorById(Long id, EditorRequestTo patchEditorDto) {
        return editorService.patchEditorById(id, patchEditorDto);
    }

}
