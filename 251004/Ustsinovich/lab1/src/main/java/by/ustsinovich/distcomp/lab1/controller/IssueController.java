package by.ustsinovich.distcomp.lab1.controller;

import by.ustsinovich.distcomp.lab1.dto.request.IssueRequestTo;
import by.ustsinovich.distcomp.lab1.dto.response.IssueResponseTo;
import by.ustsinovich.distcomp.lab1.dto.specification.filter.IssueFilterCriteria;
import by.ustsinovich.distcomp.lab1.dto.specification.pagination.PaginationCriteria;
import by.ustsinovich.distcomp.lab1.dto.specification.sort.IssueSortCriteria;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface IssueController {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    IssueResponseTo createIssue(@RequestBody IssueRequestTo createIssueDto);

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    List<IssueResponseTo> findAllIssues(
            IssueFilterCriteria filterCriteria,
            List<IssueSortCriteria> sortCriteria,
            PaginationCriteria paginationCriteria
    );

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    IssueResponseTo findIssueById(@PathVariable Long id);

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    IssueResponseTo updateIssueById(@PathVariable Long id, @RequestBody IssueRequestTo updateIssueDto);

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    void deleteIssueById(@PathVariable Long id);

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{id}")
    IssueResponseTo patchIssueById(@PathVariable Long id, @RequestBody IssueRequestTo patchIssueDto);

}
