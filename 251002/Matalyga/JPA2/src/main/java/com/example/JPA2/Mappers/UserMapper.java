package com.example.JPA2.Mappers;

import com.example.JPA2.Impl.DTO.UserRequestTo;
import com.example.JPA2.Impl.DTO.UserResponseTo;
import com.example.JPA2.Impl.Entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    //UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    UserResponseTo UserToUserResponseTo(User user);
    UserRequestTo UserToUserRequestTo(User user);
    User UserResponseToToUser(UserResponseTo userResponseTo);
    User UserRequestToToUser(UserRequestTo userRequestTo);
}
