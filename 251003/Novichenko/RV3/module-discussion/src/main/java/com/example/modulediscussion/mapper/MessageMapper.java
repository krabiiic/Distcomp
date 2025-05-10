package com.example.modulediscussion.mapper;

import com.example.modulediscussion.dto.MessageDTO;
import com.example.modulediscussion.entity.Message;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    Message toMessage(MessageDTO messageDTO);
    MessageDTO toMessageDTO(Message message);
    List<Message> toMessageList(List<MessageDTO> messageDTOS);
    List<MessageDTO>toMessageDTOList(List<Message> messages);
}
