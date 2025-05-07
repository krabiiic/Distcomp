package by.zdanovich.Publisher.utils.mappers;

import by.zdanovich.Publisher.DTOs.Requests.MarkRequestDTO;
import by.zdanovich.Publisher.DTOs.Responses.MarkResponseDTO;
import by.zdanovich.Publisher.models.Mark;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MarkMapper {
    MarkResponseDTO toMarkResponse(Mark mark);

    List<MarkResponseDTO> toMarkResponseList(List<Mark> marks);

    Mark toMark(MarkRequestDTO markRequestDTO);

    List<Mark> toMarkList(List<MarkRequestDTO> markRequestDTOList);
}
