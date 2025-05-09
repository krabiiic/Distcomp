package com.discussion.rvlab4_discussion.mapper;

import com.discussion.rvlab4_discussion.dto.MessageRequestTo;
import com.discussion.rvlab4_discussion.dto.MessageResponseTo;
import com.discussion.rvlab4_discussion.entity.Message;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MessageMapper {

    MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);

    Message toEntity(MessageRequestTo messageRequestTo);

    MessageResponseTo toResponseDto(Message message);

    Message toEntity(MessageResponseTo messageResponseTo);

    MessageRequestTo toRequestDto(Message message);
}
