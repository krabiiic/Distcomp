package lab.mappers;
import lab.modelDTOs.PostRequestTo;
import lab.modelDTOs.PostResponseTo;
import lab.models.Issue;
import lab.models.Post;
import lab.models.User;
import lab.services.IssueService;
import lab.services.UserService;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PostMapper {

    PostMapper INSTANCE = Mappers.getMapper( PostMapper.class );

    @Mapping(source = "issue.id", target = "issueId")
    PostRequestTo MapPostToRequestDTO(Post post);
    @Mapping(source = "issue.id", target = "issueId")
    PostResponseTo MapPostToResponseDTO(Post post);


    @Mapping(source = "issueId", target = "issue", qualifiedByName = "mapIssue")
    Post MapResponseDTOToPost(PostResponseTo postResponseTo);
    @Mapping(source = "issueId", target = "issue", qualifiedByName = "mapIssue")
    Post MapRequestDTOToPost(PostRequestTo postRequestTo);

    PostResponseTo MapRequestDTOToResponseDTO(PostRequestTo postRequestTo);

    @Named("mapIssue")
    default Issue mapUser(Long issueId) {
        if (issueId == null)
            return null;
        Issue issue = new Issue();
        issue.setId(issueId);
        return issue;
    }
}