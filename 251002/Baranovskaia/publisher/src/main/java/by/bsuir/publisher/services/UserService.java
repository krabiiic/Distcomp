package by.bsuir.publisher.services;

import by.bsuir.publisher.dto.requests.UserRequestDto;
import by.bsuir.publisher.dto.responses.UserResponseDto;

import java.util.List;

public interface UserService extends BaseService<UserRequestDto, UserResponseDto> {
    List<UserResponseDto> readAll();
}
