package by.ustsinovich.distcomp.lab1.controller.impl.v1;

import by.ustsinovich.distcomp.lab1.controller.IssueController;
import by.ustsinovich.distcomp.lab1.dto.request.IssueRequestTo;
import by.ustsinovich.distcomp.lab1.dto.response.IssueResponseTo;
import by.ustsinovich.distcomp.lab1.dto.specification.filter.IssueFilterCriteria;
import by.ustsinovich.distcomp.lab1.dto.specification.pagination.PaginationCriteria;
import by.ustsinovich.distcomp.lab1.dto.specification.sort.IssueSortCriteria;
import by.ustsinovich.distcomp.lab1.service.IssueService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1.0/issues")
@RestController
public class IssueControllerV1 implements IssueController {

    private final IssueService issueService;

    @Override
    public IssueResponseTo createIssue(IssueRequestTo createIssueDto) {
        return null;
    }

    @Override
    public List<IssueResponseTo> findAllIssues(
            IssueFilterCriteria filterCriteria,
            List<IssueSortCriteria> sortCriteria,
            PaginationCriteria paginationCriteria
    ) {
        return List.of();
    }

    @Override
    public IssueResponseTo findIssueById(Long id) {
        return null;
    }

    @Override
    public IssueResponseTo updateIssueById(Long id, IssueRequestTo updateIssueDto) {
        return null;
    }

    @Override
    public void deleteIssueById(Long id) {

    }

    @Override
    public IssueResponseTo patchIssueById(Long id, IssueRequestTo patchIssueDto) {
        return null;
    }

}
