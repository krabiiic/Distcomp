package by.bsuir.publisher.dto.responses.converters;

import by.bsuir.publisher.domain.User;
import by.bsuir.publisher.dto.responses.UserResponseDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = UserResponseConverter.class)
public interface CollectionUserResponseConverter {
    List<UserResponseDto> toListDto(List<User> users);
}