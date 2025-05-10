package by.bsuir.publisher.dto.requests.converters;

import by.bsuir.publisher.domain.User;
import by.bsuir.publisher.dto.requests.UserRequestDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserRequestConverter {
    User fromDto(UserRequestDto user);
}
