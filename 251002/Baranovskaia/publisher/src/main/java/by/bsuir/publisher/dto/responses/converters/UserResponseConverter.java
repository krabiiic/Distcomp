package by.bsuir.publisher.dto.responses.converters;

import by.bsuir.publisher.domain.User;
import by.bsuir.publisher.dto.responses.UserResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserResponseConverter {
    UserResponseDto toDto(User user);
}
