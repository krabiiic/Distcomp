package com.example.modulepublisher.service;

import com.example.modulepublisher.dto.UserDTO;
import com.example.modulepublisher.entity.User;
import com.example.modulepublisher.exception.DublExeption;
import com.example.modulepublisher.exception.MyException;
import com.example.modulepublisher.mapper.UserMapper;
import com.example.modulepublisher.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public UserDTO createUser(UserDTO userDTO){
        User user = userMapper.toUser(userDTO);
        Optional<User> odubl = userRepository.findUserByLogin(user.getLogin());
        if(odubl.isPresent()){
            throw new DublExeption("aaaaa");
        }
        userRepository.save(user);
        UserDTO dto = userMapper.toUserDTO(user);
        return  dto;
    }

    public UserDTO deleteUser(int id) throws Exception {
        Optional<User> ouser = userRepository.findUserById(id);
        User user = ouser.orElseThrow(() -> new MyException("aaaaaa"));
        UserDTO dto = userMapper.toUserDTO(user);
        userRepository.delete(user);
        return  dto;
    }

    public UserDTO getUser(int id){
        Optional<User> ouser = userRepository.findUserById(id);
        User user = ouser.orElseThrow(() -> new MyException("aaaaaa"));
        UserDTO dto = userMapper.toUserDTO(user);
        return  dto;
    }

    public List<UserDTO> getUsers(){
        List<User> users = userRepository.findAll();
        List<UserDTO> dtos = userMapper.toUserDTOList(users);
        return  dtos;
    }

    public UserDTO updateUser(UserDTO userDTO){
        User user = userMapper.toUser(userDTO);
        userRepository.save(user);
        UserDTO dto = userMapper.toUserDTO(user);
        return  dto;
    }



}
