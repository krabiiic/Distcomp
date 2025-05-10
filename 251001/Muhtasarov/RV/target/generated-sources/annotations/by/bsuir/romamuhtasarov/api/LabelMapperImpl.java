package by.bsuir.romamuhtasarov.api;

import by.bsuir.romamuhtasarov.impl.bean.Label;
import by.bsuir.romamuhtasarov.impl.dto.LabelRequestTo;
import by.bsuir.romamuhtasarov.impl.dto.LabelResponseTo;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-02-17T17:15:23+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 18.0.2.1 (Oracle Corporation)"
)
public class LabelMapperImpl implements LabelMapper {

    @Override
    public Label LabelResponseToToLabel(LabelResponseTo responseTo) {
        if ( responseTo == null ) {
            return null;
        }

        Label label = new Label();

        label.setId( responseTo.getId() );
        label.setName( responseTo.getName() );

        return label;
    }

    @Override
    public Label LabelRequestToToLabel(LabelRequestTo requestTo) {
        if ( requestTo == null ) {
            return null;
        }

        Label label = new Label();

        label.setId( requestTo.getId() );
        label.setName( requestTo.getName() );

        return label;
    }

    @Override
    public LabelRequestTo LabelToLabelRequestTo(Label Label) {
        if ( Label == null ) {
            return null;
        }

        LabelRequestTo labelRequestTo = new LabelRequestTo();

        labelRequestTo.setId( Label.getId() );
        labelRequestTo.setName( Label.getName() );

        return labelRequestTo;
    }

    @Override
    public LabelResponseTo LabelToLabelResponseTo(Label Label) {
        if ( Label == null ) {
            return null;
        }

        LabelResponseTo labelResponseTo = new LabelResponseTo();

        labelResponseTo.setId( Label.getId() );
        labelResponseTo.setName( Label.getName() );

        return labelResponseTo;
    }
}
