package by.kardychka.Publisher.utils.mappers;

import by.kardychka.Publisher.DTOs.Requests.CreatorRequestDTO;
import by.kardychka.Publisher.DTOs.Responses.CreatorResponseDTO;
import by.kardychka.Publisher.models.Creator;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CreatorsMapper {
    CreatorResponseDTO toCreatorResponse(Creator creator);
    List<CreatorResponseDTO> toCreatorResponseList(List<Creator> creators);
    Creator toCreator(CreatorRequestDTO creatorRequestDTO);
}
