package by.zdanovich.Publisher.utils.mappers;

import by.zdanovich.Publisher.DTOs.Requests.IssueRequestDTO;
import by.zdanovich.Publisher.DTOs.Responses.IssueResponseDTO;
import by.zdanovich.Publisher.models.Issue;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-07T21:47:14+0300",
    comments = "version: 1.6.0, compiler: javac, environment: Java 23.0.2 (Oracle Corporation)"
)
@Component
public class IssueMapperImpl implements IssueMapper {

    @Override
    public IssueResponseDTO toIssueResponse(Issue issue) {
        if ( issue == null ) {
            return null;
        }

        IssueResponseDTO issueResponseDTO = new IssueResponseDTO();

        issueResponseDTO.setId( issue.getId() );
        issueResponseDTO.setTitle( issue.getTitle() );
        issueResponseDTO.setContent( issue.getContent() );
        issueResponseDTO.setCreated( issue.getCreated() );
        issueResponseDTO.setModified( issue.getModified() );

        issueResponseDTO.setWriterId( issue.getWriter().getId() );
        issueResponseDTO.setMarks( issue.getMarks().stream().map(mark -> mark.getName()).toList() );

        return issueResponseDTO;
    }

    @Override
    public List<IssueResponseDTO> toIssueResponseList(List<Issue> issues) {
        if ( issues == null ) {
            return null;
        }

        List<IssueResponseDTO> list = new ArrayList<IssueResponseDTO>( issues.size() );
        for ( Issue issue : issues ) {
            list.add( toIssueResponse( issue ) );
        }

        return list;
    }

    @Override
    public Issue toIssue(IssueRequestDTO issueRequestDTO) {
        if ( issueRequestDTO == null ) {
            return null;
        }

        Issue issue = new Issue();

        if ( issueRequestDTO.getId() != null ) {
            issue.setId( issueRequestDTO.getId() );
        }
        issue.setTitle( issueRequestDTO.getTitle() );
        issue.setContent( issueRequestDTO.getContent() );

        return issue;
    }
}
