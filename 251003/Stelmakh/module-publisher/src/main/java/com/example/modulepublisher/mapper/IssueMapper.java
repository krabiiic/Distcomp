package com.example.modulepublisher.mapper;

import com.example.modulepublisher.dto.IssueDTO;
import com.example.modulepublisher.entity.Issue;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IssueMapper {
   Issue toIssue(IssueDTO issueDTO);
   IssueDTO toIssueDTO(Issue issue);
   List<Issue> toIssueList(List<IssueDTO>issueDTOS);
   List<IssueDTO> toIssueDTOLost(List<Issue> issues);
}
