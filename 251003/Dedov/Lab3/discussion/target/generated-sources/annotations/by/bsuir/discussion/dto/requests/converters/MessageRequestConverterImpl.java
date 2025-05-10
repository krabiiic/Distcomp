package by.bsuir.discussion.dto.requests.converters;

import by.bsuir.discussion.domain.Message;
import by.bsuir.discussion.dto.requests.MessageRequestDto;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-09T00:41:42+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.2 (Oracle Corporation)"
)
@Component
public class MessageRequestConverterImpl implements MessageRequestConverter {

    @Override
    public Message fromDto(MessageRequestDto message) {
        if ( message == null ) {
            return null;
        }

        Message.MessageBuilder<?, ?> message1 = Message.builder();

        message1.id( message.getId() );
        message1.newsId( message.getNewsId() );
        message1.content( message.getContent() );

        return message1.build();
    }
}
