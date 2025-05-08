package com.example.rv1.mapper;

import com.example.rv1.dto.MessageDTO;
import com.example.rv1.entity.Message;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-02T18:43:27+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.2 (Oracle Corporation)"
)
@Component
public class MessageMapperImpl implements MessageMapper {

    @Override
    public Message toMessage(MessageDTO messageDTO) {
        if ( messageDTO == null ) {
            return null;
        }

        Message message = new Message();

        message.setId( messageDTO.getId() );
        message.setNewsId( messageDTO.getNewsId() );
        message.setContent( messageDTO.getContent() );

        return message;
    }

    @Override
    public MessageDTO toMessageDTO(Message message) {
        if ( message == null ) {
            return null;
        }

        MessageDTO messageDTO = new MessageDTO();

        messageDTO.setId( message.getId() );
        messageDTO.setNewsId( message.getNewsId() );
        messageDTO.setContent( message.getContent() );

        return messageDTO;
    }

    @Override
    public List<Message> toMessageList(List<MessageDTO> messageDTOS) {
        if ( messageDTOS == null ) {
            return null;
        }

        List<Message> list = new ArrayList<Message>( messageDTOS.size() );
        for ( MessageDTO messageDTO : messageDTOS ) {
            list.add( toMessage( messageDTO ) );
        }

        return list;
    }

    @Override
    public List<MessageDTO> toMessageDTOList(List<Message> messages) {
        if ( messages == null ) {
            return null;
        }

        List<MessageDTO> list = new ArrayList<MessageDTO>( messages.size() );
        for ( Message message : messages ) {
            list.add( toMessageDTO( message ) );
        }

        return list;
    }
}
