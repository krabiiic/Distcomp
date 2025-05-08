package com.example.Rest1.Mappers;

import com.example.Rest1.Impl.Entity.User;
import com.example.Rest1.Impl.DTO.UserRequestTo;
import com.example.Rest1.Impl.DTO.UserResponseTo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    UserResponseTo UserToUserResponseTo(User user);
    UserRequestTo UserToUserRequestTo(User user);
    User UserResponseToToUser(UserResponseTo userResponseTo);
    User UserRequestToToUser(UserRequestTo userRequestTo);
}
