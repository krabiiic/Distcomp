package org.example.Mappers;

import org.example.Impl.DTO.UserRequestTo;
import org.example.Impl.DTO.UserResponseTo;
import org.example.Impl.Entity.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface UserListMapper {
    List<User> toUserList(List<UserRequestTo> users);

    List<UserResponseTo> toUserResponseList(List<User> users);
}
