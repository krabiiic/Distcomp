package lab1.Mappers;
import lab1.modelDTOs.IssueRequestTo;
import lab1.modelDTOs.IssueResponseTo;
import lab1.models.Issue;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface IssueMapper {

    IssueMapper INSTANCE = Mappers.getMapper( IssueMapper.class );

    IssueRequestTo MapIssueToRequestDTO(Issue issue);
    IssueResponseTo MapIssueToResponseDTO(Issue issue);

    Issue MapResponseDTOToIssue(IssueResponseTo issueResponseTo);
    Issue MapRequestDTOToIssue(IssueRequestTo issueRequestTo);
}