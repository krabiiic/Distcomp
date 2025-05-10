package discussion.mappers;
import discussion.modelDTOs.PostRequestTo;
import discussion.modelDTOs.PostResponseTo;
import discussion.models.Post;
import discussion.models.PostKey;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface PostMapper {
    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);

    @Mapping(target = "country", source = "id.country")
    @Mapping(target = "issueId", source = "id.issueId")
    @Mapping(target = "id", source = "id.id")
    PostResponseTo MapPostToResponseDTO(Post post);

    @Mapping(target = "id", source = "postRequestTo", qualifiedByName = "mapKey")
    Post MapRequestDTOToPost(PostRequestTo postRequestTo);

    @Named("mapKey")
    default PostKey mapKey(PostRequestTo dto) {
        String country = dto.getCountry() == null || dto.getCountry().isBlank() ? "default" : dto.getCountry();
        Long id = (dto.getId() != null && dto.getId() != 0) ? dto.getId() : generateId();
        return new PostKey(country, dto.getIssueId(), id);
    }

    private Long generateId() {
        return Math.abs(UUID.randomUUID().getMostSignificantBits());
    }
}