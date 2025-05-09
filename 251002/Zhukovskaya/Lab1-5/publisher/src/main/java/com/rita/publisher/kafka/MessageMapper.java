package com.rita.publisher.kafka;

import com.rita.publisher.dto.MessageCreateDTO;
import com.rita.publisher.dto.MessageDTO;
import com.rita.publisher.dto.MessageUpdateDTO;
import com.rita.publisher.mapper.MapperUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,uses = {
        MapperUtil.class
})
public interface MessageMapper {
    //@Mapping(target = "stickerIds", qualifiedByName = {"MapperUtil", "getStickerIds"},source = "id")
    @Mapping(target = "id",ignore = true)
    //@Mapping(target = "state",ignore = true)
    Message toMessage(MessageCreateDTO messageCreateDTO);
    //@Mapping(target = "stickerIds", qualifiedByName = {"MapperUtil", "getStickerIds"},source = "id")
    //@Mapping(target = "state",ignore = true)
    Message toMessage(MessageUpdateDTO messageUpdateDTO);
    MessageDTO toMessageDTO(Message message);
}
