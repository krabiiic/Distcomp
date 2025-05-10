package by.bsuir.discussion.dto.responses.converters;

import by.bsuir.discussion.domain.Message;
import by.bsuir.discussion.dto.responses.MessageResponseDto;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-09T01:08:34+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.15 (Eclipse Adoptium)"
)
@Component
public class CollectionMessageResponseConverterImpl implements CollectionMessageResponseConverter {

    @Autowired
    private MessageResponseConverter messageResponseConverter;

    @Override
    public List<MessageResponseDto> toListDto(List<Message> messages) {
        if ( messages == null ) {
            return null;
        }

        List<MessageResponseDto> list = new ArrayList<MessageResponseDto>( messages.size() );
        for ( Message message : messages ) {
            list.add( messageResponseConverter.toDto( message ) );
        }

        return list;
    }
}
