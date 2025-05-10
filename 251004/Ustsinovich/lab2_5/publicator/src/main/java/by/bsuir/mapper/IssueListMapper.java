package by.bsuir.mapper;

import by.bsuir.dto.IssueRequestTo;
import by.bsuir.dto.IssueResponseTo;
import by.bsuir.entities.Issue;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = IssueMapper.class)
public interface IssueListMapper {
    List<Issue> toIssueList(List<IssueRequestTo> issues);
    List<IssueResponseTo> toIssueResponseList(List<Issue> issues);
}
