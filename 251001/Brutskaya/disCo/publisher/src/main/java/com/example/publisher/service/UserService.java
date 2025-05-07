package com.example.publisher.service;

import com.example.publisher.api.dto.request.UserRequestTo;
import com.example.publisher.api.dto.responce.UserResponseTo;
import com.example.publisher.mapper.UserMapper;
import com.example.publisher.model.User;
import com.example.publisher.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserResponseTo create(UserRequestTo request) {
        if (userRepository.existsByLogin(request.getLogin())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        User user = userMapper.fromRequestToEntity(request);
        return userMapper.fromEntityToResponse(userRepository.save(user));
    }

    public List<UserResponseTo> getAll(){
        return userRepository.findAll().stream()
                .map(userMapper::fromEntityToResponse)
                .toList();
    }

    public UserResponseTo getById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::fromEntityToResponse)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
    }

    public UserResponseTo update(UserRequestTo request) {
        User entity = userRepository.findById(request.getId())
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        userMapper.updateEntity(entity, request);
        return userMapper.fromEntityToResponse(userRepository.save(entity));
    }

    public void delete(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new NoSuchElementException("User not found");
        }
    }
}
