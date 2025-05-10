package by.bsuir.discussion.dto.responses.converters;

import by.bsuir.discussion.domain.Message;
import by.bsuir.discussion.dto.responses.MessageResponseDto;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-09T00:41:42+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.2 (Oracle Corporation)"
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
