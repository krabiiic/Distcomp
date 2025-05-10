package publisher.mappers;

import org.mapstruct.MappingConstants;
import publisher.modelDTOs.PostRequestTo;
import publisher.modelDTOs.PostResponseTo;
import publisher.models.Issue;
import publisher.models.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
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