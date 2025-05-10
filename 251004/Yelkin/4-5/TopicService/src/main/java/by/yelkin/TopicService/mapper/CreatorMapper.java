package by.yelkin.TopicService.mapper;

import by.yelkin.TopicService.dto.creator.CreatorRequestTo;
import by.yelkin.TopicService.dto.creator.CreatorResponseTo;
import by.yelkin.TopicService.entity.Creator;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CreatorMapper {

    Creator toCreator(CreatorRequestTo creatorRequestTo);
    CreatorResponseTo toCreatorResponseTo(Creator creator);
}

