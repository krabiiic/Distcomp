package by.bsuir.publisher.dto.responses.converters;

import by.bsuir.publisher.domain.Topic;
import by.bsuir.publisher.dto.responses.TopicResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TopicResponseConverter {
    @Mapping(source = "user.id", target = "userId")
    TopicResponseDto toDto(Topic topic);
}
