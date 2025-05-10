package by.bsuir.discussion.dto.responses.converters;

import by.bsuir.discussion.domain.Message;
import by.bsuir.discussion.dto.responses.MessageResponseDto;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-09T01:08:34+0300",
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
        messageResponseDto.newsId( message.getNewsId() );
        messageResponseDto.content( message.getContent() );

        return messageResponseDto.build();
    }
}
