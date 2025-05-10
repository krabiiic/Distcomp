package lab1.Mappers;
import lab1.modelDTOs.TagRequestTo;
import lab1.modelDTOs.TagResponseTo;
import lab1.models.Tag;
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