package by.bsuir.publisher.dto.requests.converters;

import by.bsuir.publisher.domain.Topic;
import by.bsuir.publisher.dto.requests.TopicRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TopicRequestConverter {
    @Mapping(source = "userId", target = "user.id")
    Topic fromDto(TopicRequestDto topic);
}
