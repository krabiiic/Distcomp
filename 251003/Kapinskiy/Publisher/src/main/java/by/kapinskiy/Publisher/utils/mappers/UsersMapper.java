package by.kapinskiy.Publisher.utils.mappers;

import by.kapinskiy.Publisher.DTOs.Requests.UserRequestDTO;
import by.kapinskiy.Publisher.DTOs.Responses.UserResponseDTO;
import by.kapinskiy.Publisher.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UsersMapper {
    UserResponseDTO toUserResponse(User user);

    List<UserResponseDTO> toUserResponseList(List<User> users);

    User toUser(UserRequestDTO userRequestDTO);
}
