package org.example.Mappers;

import org.example.Impl.DTO.UserRequestTo;
import org.example.Impl.DTO.UserResponseTo;
import org.example.Impl.Entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    //UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    UserResponseTo UserToUserResponseTo(User user);
    UserRequestTo UserToUserRequestTo(User user);
    User UserResponseToToUser(UserResponseTo userResponseTo);
    User UserRequestToToUser(UserRequestTo userRequestTo);
}
