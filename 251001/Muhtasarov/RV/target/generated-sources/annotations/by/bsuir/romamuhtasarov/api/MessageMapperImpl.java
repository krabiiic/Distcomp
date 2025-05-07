package by.bsuir.romamuhtasarov.api;

import by.bsuir.romamuhtasarov.impl.bean.Message;
import by.bsuir.romamuhtasarov.impl.dto.MessageRequestTo;
import by.bsuir.romamuhtasarov.impl.dto.MessageResponseTo;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-02-17T17:15:23+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 18.0.2.1 (Oracle Corporation)"
)
public class MessageMapperImpl implements MessageMapper {

    @Override
    public Message MessageResponseToToMessage(MessageResponseTo messageResponseTo) {
        if ( messageResponseTo == null ) {
            return null;
        }

        Message message = new Message();

        message.setId( messageResponseTo.getId() );
        message.setContent( messageResponseTo.getContent() );
        message.setNewsId( messageResponseTo.getNewsId() );

        return message;
    }

    @Override
    public Message MessageRequestToToMessage(MessageRequestTo messageRequestTo) {
        if ( messageRequestTo == null ) {
            return null;
        }

        Message message = new Message();

        message.setId( messageRequestTo.getId() );
        message.setContent( messageRequestTo.getContent() );
        message.setNewsId( messageRequestTo.getNewsId() );

        return message;
    }

    @Override
    public MessageResponseTo MessageToMessageResponseTo(Message message) {
        if ( message == null ) {
            return null;
        }

        MessageResponseTo messageResponseTo = new MessageResponseTo();

        messageResponseTo.setId( message.getId() );
        messageResponseTo.setContent( message.getContent() );
        messageResponseTo.setNewsId( message.getNewsId() );

        return messageResponseTo;
    }

    @Override
    public MessageRequestTo MessageToMessageRequestTo(Message message) {
        if ( message == null ) {
            return null;
        }

        MessageRequestTo messageRequestTo = new MessageRequestTo();

        messageRequestTo.setId( message.getId() );
        messageRequestTo.setContent( message.getContent() );
        messageRequestTo.setNewsId( message.getNewsId() );

        return messageRequestTo;
    }
}
