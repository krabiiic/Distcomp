package by.kapinskiy.Publisher.utils.mappers;

import by.kapinskiy.Publisher.DTOs.Requests.TagRequestDTO;
import by.kapinskiy.Publisher.DTOs.Responses.TagResponseDTO;
import by.kapinskiy.Publisher.models.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TagsMapper {
    TagResponseDTO toTagResponse(Tag tag);

    List<TagResponseDTO> toTagResponseList(List<Tag> tags);

    Tag toTag(TagRequestDTO tagRequestDTO);

    List<Tag> toTagList(List<TagRequestDTO> tagRequestDTOList);
}
