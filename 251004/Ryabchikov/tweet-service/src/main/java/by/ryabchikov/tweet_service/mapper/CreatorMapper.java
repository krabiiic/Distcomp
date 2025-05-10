package by.ryabchikov.tweet_service.mapper;

import by.ryabchikov.tweet_service.dto.creator.CreatorRequestTo;
import by.ryabchikov.tweet_service.dto.creator.CreatorResponseTo;
import by.ryabchikov.tweet_service.entity.Creator;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CreatorMapper {

    @Mapping(target = "id", ignore = true)
    Creator toCreator(CreatorRequestTo creatorRequestTo);
    CreatorResponseTo toCreatorResponseTo(Creator creator);
}
