package by.bsuir.romamuhtasarov.api;

import by.bsuir.romamuhtasarov.impl.bean.Creator;
import by.bsuir.romamuhtasarov.impl.dto.CreatorRequestTo;
import by.bsuir.romamuhtasarov.impl.dto.CreatorResponseTo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CreatorMapper {
    CreatorMapper INSTANCE = Mappers.getMapper(CreatorMapper.class);

    CreatorResponseTo CreatorToCreatorResponseTo(Creator creator);

    CreatorRequestTo CreatorToCreatorRequestTo(Creator creator);

    Creator CreatorResponseToToCreator(CreatorResponseTo creatorResponseTo);

    Creator CreatorRequestToToCreator(CreatorRequestTo creatorRequestTo);
}