package lab.mappers;
import lab.modelDTOs.IssueRequestTo;
import lab.modelDTOs.IssueResponseTo;
import lab.models.Issue;
import lab.models.Tag;
import lab.models.User;
import lab.repository.UserDao;
import lab.services.TagService;
import lab.services.UserService;
import lab.services.implementations.DefaultUserService;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.control.MappingControl;
import org.mapstruct.factory.Mappers;

import java.util.HashSet;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface IssueMapper {

    IssueMapper INSTANCE = Mappers.getMapper( IssueMapper.class );

    @Mapping(source = "user.id", target = "userId")
    @Mapping(target = "tags", source = "tags", qualifiedByName = "mapTagNames")
    IssueRequestTo MapIssueToRequestDTO(Issue issue);
    @Mapping(source = "user.id", target = "userId")
    @Mapping(target = "tags", source = "tags", qualifiedByName = "mapTagNames")
    IssueResponseTo MapIssueToResponseDTO(Issue issue);

    @Mapping(source = "userId", target = "user", qualifiedByName = "mapUser")
    @Mapping(target = "tags", source = "tags", qualifiedByName = "mapTags")
    Issue MapResponseDTOToIssue(IssueResponseTo issueResponseTo, @Context TagService tagService);

    @Mapping(target = "tags", source = "tags", qualifiedByName = "mapTags")
    @Mapping(source = "userId", target = "user", qualifiedByName = "mapUser")
    Issue MapRequestDTOToIssue(IssueRequestTo issueRequestTo, @Context TagService tagService);

    IssueResponseTo MapRequestDTOToResponseDTO(IssueRequestTo issueRequestTo);

    @Named("mapUser")
    default User mapUser(Long userId) {
        if (userId == null)
            return null;
        User user = new User();
        user.setId(userId);
        return user;
    }

    @Named("mapTags")
    default Set<Tag> mapTags(Set<String> tagNames, @Context TagService tagService) {
        if (tagNames == null) return null;
        Set<Tag> tags = new HashSet<>();
        for (String tagName : tagNames) {
            tags.add(tagService.findOrCreateTag(tagName));  // Используем метод поиска или создания тегов
        }
        return tags;
    }

    @Named("mapTagNames")
    default Set<String> mapTagNames(Set<Tag> tags) {
        if (tags == null) return null;
        Set<String> tagNames = new HashSet<>();
        for (Tag tag : tags) {
            tagNames.add(tag.getName());
        }
        return tagNames;
    }
}