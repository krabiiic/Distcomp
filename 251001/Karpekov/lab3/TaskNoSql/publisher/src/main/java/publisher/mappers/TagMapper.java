package publisher.mappers;
import publisher.modelDTOs.TagRequestTo;
import publisher.modelDTOs.TagResponseTo;
import publisher.models.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TagMapper {

    TagMapper INSTANCE = Mappers.getMapper( TagMapper.class );

    TagRequestTo MapTagToRequestDTO(Tag tag);
    TagResponseTo MapTagToResponseDTO(Tag tag);

    Tag MapResponseDTOToTag(TagResponseTo tagResponseTo);
    Tag MapRequestDTOToTag(TagRequestTo tagRequestTo);
}