package by.zdanovich.Publisher.utils.mappers;

import by.zdanovich.Publisher.DTOs.Requests.MessageRequestDTO;
import by.zdanovich.Publisher.DTOs.Responses.MessageResponseDTO;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-07T21:47:14+0300",
    comments = "version: 1.6.0, compiler: javac, environment: Java 23.0.2 (Oracle Corporation)"
)
@Component
public class MessageMapperImpl implements MessageMapper {

    @Override
    public MessageResponseDTO toMarkResponse(MessageRequestDTO messageRequestDTO) {
        if ( messageRequestDTO == null ) {
            return null;
        }

        MessageResponseDTO messageResponseDTO = new MessageResponseDTO();

        messageResponseDTO.setId( messageRequestDTO.getId() );
        messageResponseDTO.setIssueId( messageRequestDTO.getIssueId() );
        messageResponseDTO.setContent( messageRequestDTO.getContent() );

        return messageResponseDTO;
    }
}
