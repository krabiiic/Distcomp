package by.zdanovich.Publisher.utils.mappers;

import by.zdanovich.Publisher.DTOs.Requests.WriterRequestDTO;
import by.zdanovich.Publisher.DTOs.Responses.WriterResponseDTO;
import by.zdanovich.Publisher.models.Writer;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface WriterMapper {
    WriterResponseDTO toWriterResponse(Writer writer);

    List<WriterResponseDTO> toWriterResponseList(List<Writer> writers);

    Writer toWriter(WriterRequestDTO userRequestDTO);
}
