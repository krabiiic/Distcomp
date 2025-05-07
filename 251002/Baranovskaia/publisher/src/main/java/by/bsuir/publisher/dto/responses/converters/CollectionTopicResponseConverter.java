package by.bsuir.publisher.dto.responses.converters;

import by.bsuir.publisher.domain.Topic;
import by.bsuir.publisher.dto.responses.TopicResponseDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = TopicResponseConverter.class)
public interface CollectionTopicResponseConverter {
    List<TopicResponseDto> toListDto(List<Topic> labels);
}
