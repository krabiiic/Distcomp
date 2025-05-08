package com.example.JPA2.Mappers;

import com.example.JPA2.Impl.DTO.UserRequestTo;
import com.example.JPA2.Impl.DTO.UserResponseTo;
import com.example.JPA2.Impl.Entity.User;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface UserListMapper {
    List<User> toUserList(List<UserRequestTo> users);

    List<UserResponseTo> toUserResponseList(List<User> users);
}
