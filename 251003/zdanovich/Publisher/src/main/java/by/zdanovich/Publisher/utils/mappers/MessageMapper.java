package by.zdanovich.Publisher.utils.mappers;

import by.zdanovich.Publisher.DTOs.Requests.MessageRequestDTO;
import by.zdanovich.Publisher.DTOs.Responses.MessageResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MessageMapper {
    @Mapping(target = "id", expression = "java(messageRequestDTO.getId())")
    @Mapping(target = "issueId", expression = "java(messageRequestDTO.getIssueId())")
    @Mapping(target = "content", expression = "java(messageRequestDTO.getContent())")
    MessageResponseDTO toMarkResponse(MessageRequestDTO messageRequestDTO);
}
