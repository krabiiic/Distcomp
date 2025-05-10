package com.example.publisher.mapper;

import com.example.publisher.api.dto.request.UserRequestTo;
import com.example.publisher.api.dto.responce.UserResponseTo;
import com.example.publisher.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    User fromRequestToEntity(UserRequestTo request);

    UserResponseTo fromEntityToResponse(User entity);

    @Mapping(target = "id", ignore = true)
    void updateEntity(@MappingTarget User entity, UserRequestTo request);
}
