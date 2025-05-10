package by.bsuir.romamuhtasarov.api;

import by.bsuir.romamuhtasarov.impl.bean.Creator;
import by.bsuir.romamuhtasarov.impl.dto.CreatorRequestTo;
import by.bsuir.romamuhtasarov.impl.dto.CreatorResponseTo;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-02-17T17:15:23+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 18.0.2.1 (Oracle Corporation)"
)
public class CreatorMapperImpl implements CreatorMapper {

    @Override
    public CreatorResponseTo CreatorToCreatorResponseTo(Creator creator) {
        if ( creator == null ) {
            return null;
        }

        CreatorResponseTo creatorResponseTo = new CreatorResponseTo();

        creatorResponseTo.setId( creator.getId() );
        creatorResponseTo.setLogin( creator.getLogin() );
        creatorResponseTo.setPassword( creator.getPassword() );
        creatorResponseTo.setFirstname( creator.getFirstname() );
        creatorResponseTo.setLastname( creator.getLastname() );

        return creatorResponseTo;
    }

    @Override
    public CreatorRequestTo CreatorToCreatorRequestTo(Creator creator) {
        if ( creator == null ) {
            return null;
        }

        CreatorRequestTo creatorRequestTo = new CreatorRequestTo();

        creatorRequestTo.setId( creator.getId() );
        creatorRequestTo.setLogin( creator.getLogin() );
        creatorRequestTo.setPassword( creator.getPassword() );
        creatorRequestTo.setFirstname( creator.getFirstname() );
        creatorRequestTo.setLastname( creator.getLastname() );

        return creatorRequestTo;
    }

    @Override
    public Creator CreatorResponseToToCreator(CreatorResponseTo creatorResponseTo) {
        if ( creatorResponseTo == null ) {
            return null;
        }

        Creator creator = new Creator();

        creator.setId( creatorResponseTo.getId() );
        creator.setLogin( creatorResponseTo.getLogin() );
        creator.setPassword( creatorResponseTo.getPassword() );
        creator.setFirstname( creatorResponseTo.getFirstname() );
        creator.setLastname( creatorResponseTo.getLastname() );

        return creator;
    }

    @Override
    public Creator CreatorRequestToToCreator(CreatorRequestTo creatorRequestTo) {
        if ( creatorRequestTo == null ) {
            return null;
        }

        Creator creator = new Creator();

        creator.setId( creatorRequestTo.getId() );
        creator.setLogin( creatorRequestTo.getLogin() );
        creator.setPassword( creatorRequestTo.getPassword() );
        creator.setFirstname( creatorRequestTo.getFirstname() );
        creator.setLastname( creatorRequestTo.getLastname() );

        return creator;
    }
}
