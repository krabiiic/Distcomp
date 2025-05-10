package by.bsuir.publisher.dto.responses.converters;

import by.bsuir.publisher.domain.Message;
import by.bsuir.publisher.dto.responses.MessageResponseDto;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-09T01:08:10+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.15 (Eclipse Adoptium)"
)
@Component
public class MessageResponseConverterImpl implements MessageResponseConverter {

    @Override
    public MessageResponseDto toDto(Message message) {
        if ( message == null ) {
            return null;
        }

        MessageResponseDto.MessageResponseDtoBuilder<?, ?> messageResponseDto = MessageResponseDto.builder();

        messageResponseDto.id( message.getId() );
        messageResponseDto.content( message.getContent() );

        return messageResponseDto.build();
    }

    @Override
    public Message fromDto(MessageResponseDto messageResponseDto) {
        if ( messageResponseDto == null ) {
            return null;
        }

        Message.MessageBuilder<?, ?> message = Message.builder();

        message.id( messageResponseDto.getId() );
        message.content( messageResponseDto.getContent() );

        return message.build();
    }
}
