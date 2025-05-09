package by.yelkin.TopicService.mapper;

import by.yelkin.TopicService.dto.topic.TopicRequestTo;
import by.yelkin.TopicService.dto.topic.TopicResponseTo;
import by.yelkin.TopicService.entity.Mark;
import by.yelkin.TopicService.entity.Topic;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TopicMapper {
    @Mapping(target = "creator.id", source = "creatorId")
    @Mapping(target = "marks", source = "marks", qualifiedByName = "mapStringsToTags")
    Topic toEntity(TopicRequestTo topicRequestTo);

    @Mapping(target = "creatorId", source = "creator.id")
    @Mapping(target = "marks", source = "marks", qualifiedByName = "mapTagsToStrings")
    TopicResponseTo toResponse(Topic topic);

    @Named("mapStringsToTags")
    default List<Mark> mapStringsToTags(List<String> tagNames) {
        return tagNames.stream()
                .map(name -> {
                    Mark mark = new Mark();
                    mark.setName(name);
                    return mark;
                })
                .toList();
    }

    @Named("mapTagsToStrings")
    default List<String> mapTagsToStrings(List<Mark> marks) {
        return marks.stream()
                .map(Mark::getName)
                .toList();
    }
}