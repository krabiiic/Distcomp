package by.zdanovich.Discussion.utils.mappers;

import by.zdanovich.Discussion.DTOs.Requests.MessageRequestDTO;
import by.zdanovich.Discussion.DTOs.Responses.MessageResponseDTO;
import by.zdanovich.Discussion.models.Message;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-07T21:46:50+0300",
    comments = "version: 1.6.0, compiler: javac, environment: Java 23.0.2 (Oracle Corporation)"
)
@Component
public class MessageMapperImpl implements MessageMapper {

    @Override
    public MessageResponseDTO toMessageResponse(Message message) {
        if ( message == null ) {
            return null;
        }

        String content = null;

        content = message.getContent();

        Long id = message.getKey().getId();
        Long issueId = message.getKey().getIssueId();

        MessageResponseDTO messageResponseDTO = new MessageResponseDTO( id, issueId, content );

        return messageResponseDTO;
    }

    @Override
    public List<MessageResponseDTO> toMessageResponseList(Iterable<Message> messages) {
        if ( messages == null ) {
            return null;
        }

        List<MessageResponseDTO> list = new ArrayList<MessageResponseDTO>();
        for ( Message message : messages ) {
            list.add( toMessageResponse( message ) );
        }

        return list;
    }

    @Override
    public Message toMessage(MessageRequestDTO messageRequestDTO) {
        if ( messageRequestDTO == null ) {
            return null;
        }

        Message message = new Message();

        message.setContent( messageRequestDTO.getContent() );

        message.setKey( new Message.MessageKey(messageRequestDTO.getIssueId()) );

        return message;
    }
}
