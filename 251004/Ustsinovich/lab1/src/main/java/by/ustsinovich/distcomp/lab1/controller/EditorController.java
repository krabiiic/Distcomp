package by.ustsinovich.distcomp.lab1.controller;

import by.ustsinovich.distcomp.lab1.dto.request.EditorRequestTo;
import by.ustsinovich.distcomp.lab1.dto.response.EditorResponseTo;
import by.ustsinovich.distcomp.lab1.dto.specification.filter.EditorFilterCriteria;
import by.ustsinovich.distcomp.lab1.dto.specification.pagination.PaginationCriteria;
import by.ustsinovich.distcomp.lab1.dto.specification.sort.EditorSortCriteria;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface EditorController {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    EditorResponseTo createEditor(@RequestBody EditorRequestTo createEditorDto);

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    List<EditorResponseTo> findAllEditors(
            EditorFilterCriteria filterCriteria,
            List<EditorSortCriteria> sortCriteria,
            PaginationCriteria paginationCriteria
    );

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    EditorResponseTo findEditorById(@PathVariable Long id);

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    EditorResponseTo updateEditorById(@PathVariable Long id, @RequestBody EditorRequestTo updateEditorDto);

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    void deleteEditorById(@PathVariable Long id);

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{id}")
    EditorResponseTo patchEditorById(@PathVariable Long id, @RequestBody EditorRequestTo patchEditorDto);

}
