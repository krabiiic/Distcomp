package by.zdanovich.Publisher.utils.mappers;

import by.zdanovich.Publisher.DTOs.Requests.IssueRequestDTO;
import by.zdanovich.Publisher.DTOs.Responses.IssueResponseDTO;
import by.zdanovich.Publisher.models.Issue;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface IssueMapper {

    @Mapping(target = "writerId", expression = "java(issue.getWriter().getId())")
    @Mapping(target = "marks", expression = "java(issue.getMarks().stream().map(mark -> mark.getName()).toList())")
    IssueResponseDTO toIssueResponse(Issue issue);

    List<IssueResponseDTO> toIssueResponseList(List<Issue> issues);

    @Mapping(target = "marks", ignore = true)
    Issue toIssue(IssueRequestDTO issueRequestDTO);
}
