package com.example.rv1.mapper;

import com.example.rv1.dto.UserDTO;
import com.example.rv1.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserDTO userDTO);
    UserDTO toUserDTO(User user);
    List<User> toUserList(List<UserDTO> userDTOList);
    List<UserDTO> toUserDTOList(List<User> users);
}
