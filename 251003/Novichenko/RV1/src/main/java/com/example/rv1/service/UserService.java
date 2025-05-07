package com.example.rv1.service;

import com.example.rv1.dto.UserDTO;
import com.example.rv1.entity.User;
import com.example.rv1.exception.MyException;
import com.example.rv1.mapper.UserMapper;
import com.example.rv1.storage.InMemoryStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;

    public UserDTO createUser(UserDTO userDTO){
        User user = userMapper.toUser(userDTO);
        int amount = InMemoryStorage.users.size();
        user.setId(amount);
        InMemoryStorage.users.add(user);
        UserDTO dto = userMapper.toUserDTO(user);
        return  dto;
    }

    public UserDTO deleteUser(int id) throws Exception {
        int amount = InMemoryStorage.users.size();
        if (id >= amount){
            throw new MyException("aaaaaaaa");
        }
        User user = InMemoryStorage.users.get(id);
        UserDTO dto = userMapper.toUserDTO(user);
        InMemoryStorage.users.remove(id);
        return  dto;
    }

    public UserDTO getUser(int id){
        User user = InMemoryStorage.users.get(id);
        UserDTO dto = userMapper.toUserDTO(user);
        return  dto;
    }

    public List<UserDTO> getUsers(){
        List<User> users = InMemoryStorage.users;
        List<UserDTO> dtos = userMapper.toUserDTOList(users);
        return  dtos;
    }

    public UserDTO updateUser(UserDTO userDTO){
        User user = userMapper.toUser(userDTO);
        int id = user.getId();
        InMemoryStorage.users.set(id,user);
        UserDTO dto = userMapper.toUserDTO(user);
        return  dto;
    }



}
