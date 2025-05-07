package com.example.rv1.mapper;

import com.example.rv1.dto.MessageDTO;
import com.example.rv1.entity.Message;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    Message toMessage(MessageDTO messageDTO);
    MessageDTO toMessageDTO(Message message);
    List<Message> toMessageList(List<MessageDTO> messageDTOS);
    List<MessageDTO>toMessageDTOList(List<Message> messages);
}
