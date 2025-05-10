package by.ustsinovich.distcomp.lab1.controller;

import by.ustsinovich.distcomp.lab1.dto.request.MarkRequestTo;
import by.ustsinovich.distcomp.lab1.dto.response.MarkResponseTo;
import by.ustsinovich.distcomp.lab1.dto.specification.filter.MarkFilterCriteria;
import by.ustsinovich.distcomp.lab1.dto.specification.pagination.PaginationCriteria;
import by.ustsinovich.distcomp.lab1.dto.specification.sort.MarkSortCriteria;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface MarkController {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    MarkResponseTo createMark(@RequestBody MarkRequestTo createMarkDto);

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    List<MarkResponseTo> findAllMarks(
            MarkFilterCriteria filterCriteria,
            List<MarkSortCriteria> sortCriteria,
            PaginationCriteria paginationCriteria
    );

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    MarkResponseTo findMarkById(@PathVariable Long id);

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    MarkResponseTo updateMarkById(@PathVariable Long id, @RequestBody MarkRequestTo updateMarkDto);

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    void deleteMarkById(@PathVariable Long id);

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{id}")
    MarkResponseTo patchMarkById(@PathVariable Long id, @RequestBody MarkRequestTo patchMarkDto);

}
