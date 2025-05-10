package com.example.lab1.mapper;

import com.example.lab1.dto.UserRequestTo;
import com.example.lab1.dto.UserResponseTo;
import com.example.lab1.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserRequestTo dto);
    UserResponseTo toDto(User entity);
}
