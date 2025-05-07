package by.zdanovich.Discussion.utils.mappers;


import by.zdanovich.Discussion.DTOs.Requests.MessageRequestDTO;
import by.zdanovich.Discussion.DTOs.Responses.MessageResponseDTO;
import by.zdanovich.Discussion.models.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MessageMapper {

    @Mapping(target = "id", expression = "java(message.getKey().getId())")
    @Mapping(target = "issueId", expression = "java(message.getKey().getIssueId())")
    MessageResponseDTO toMessageResponse(Message message);

    List<MessageResponseDTO> toMessageResponseList(Iterable<Message> messages);

    @Mapping(target = "key", expression = "java(new Message.MessageKey(messageRequestDTO.getIssueId()))")
    Message toMessage(MessageRequestDTO messageRequestDTO);
}
