package by.yelkin.TopicService.mapper;

import by.yelkin.TopicService.dto.mark.MarkRequestTo;
import by.yelkin.TopicService.dto.mark.MarkResponseTo;
import by.yelkin.TopicService.entity.Mark;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MarkMapper {
    Mark toEntity(MarkRequestTo markRequestTo);
    MarkResponseTo toResponse(Mark tag);
}
