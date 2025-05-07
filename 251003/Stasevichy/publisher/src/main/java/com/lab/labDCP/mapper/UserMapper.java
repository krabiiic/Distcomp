package com.lab.labDCP.mapper;

import com.lab.labDCP.dto.UserRequestTo;
import com.lab.labDCP.dto.UserResponseTo;
import com.lab.labDCP.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User toEntity(UserRequestTo userRequestTo);

    UserResponseTo toResponseDto(User user);

    User toEntity(UserResponseTo userResponseTo);

    UserRequestTo toRequestDto(User user);
}
