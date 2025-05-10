package com.dc.mapper;

import com.dc.model.blo.Message;
import com.dc.model.dto.MessageRequestTo;
import com.dc.model.dto.MessageResponseTo;
import com.dc.model.kafka.MessageKafka;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.Collection;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface KafkaMapper {
    Message mapToBlo(MessageKafka MessageRequestTo);
    @Mapping(target = "newsId", source = "Message.news.id")
    MessageKafka mapToDto(Message Message);

    Collection<Message> mapToListBlo(Collection<MessageKafka> kList);

    Collection<MessageKafka> mapToListDto(Collection<Message> tList);
}
