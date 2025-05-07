package by.bsuir.romamuhtasarov.api;

import by.bsuir.romamuhtasarov.impl.bean.Label;
import by.bsuir.romamuhtasarov.impl.dto.LabelRequestTo;
import by.bsuir.romamuhtasarov.impl.dto.LabelResponseTo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LabelMapper {
    LabelMapper INSTANCE = Mappers.getMapper(LabelMapper.class);

    Label LabelResponseToToLabel(LabelResponseTo responseTo);

    Label LabelRequestToToLabel(LabelRequestTo requestTo);

    LabelRequestTo LabelToLabelRequestTo(Label Label);

    LabelResponseTo LabelToLabelResponseTo(Label Label);
}