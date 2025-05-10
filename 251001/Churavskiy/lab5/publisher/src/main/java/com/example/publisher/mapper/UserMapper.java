package com.example.publisher.mapper;

import com.example.publisher.dto.UserRequestTo;
import com.example.publisher.dto.UserResponseTo;
import com.example.publisher.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserResponseTo toDTO(User user);
    User toEntity(UserRequestTo userRequestTo);
}
