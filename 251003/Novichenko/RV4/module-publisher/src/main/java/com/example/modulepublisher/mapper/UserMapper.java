package com.example.modulepublisher.mapper;

import com.example.modulepublisher.dto.UserDTO;
import com.example.modulepublisher.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserDTO userDTO);
    UserDTO toUserDTO(User user);
    List<User> toUserList(List<UserDTO> userDTOList);
    List<UserDTO> toUserDTOList(List<User> users);
}
