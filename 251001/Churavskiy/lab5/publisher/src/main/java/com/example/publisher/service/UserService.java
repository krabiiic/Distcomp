package com.example.publisher.service;

import com.example.publisher.dto.UserRequestTo;
import com.example.publisher.dto.UserResponseTo;
import com.example.publisher.entity.User;
import com.example.publisher.mapper.UserMapper;
import com.example.publisher.repository.UserRepository;
import com.example.publisher.service.EntityService;
import com.example.publisher.service.exception.DuplicateLoginException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements EntityService<UserRequestTo, UserResponseTo> {

    private final UserRepository userRepository;
    private final UserMapper userMapper = UserMapper.INSTANCE;

    @Override
    public UserResponseTo create(UserRequestTo userDTO) {
        try {
            User user = userMapper.toEntity(userDTO);
            User savedUser = userRepository.save(user);
            return userMapper.toDTO(savedUser);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateLoginException("User with login '" + userDTO.login() + "' already exists");
        }
    }

    @Override
    public UserResponseTo getById(Long id) {
        return userMapper.toDTO(userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with ID " + id + " not found")));
    }

    @Override
    public List<UserResponseTo> getAll() {
        return userRepository.findAll().stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseTo update(UserRequestTo entityDTO) {
        User user = userMapper.toEntity(entityDTO);
        if (!userRepository.existsById(user.getId())) {
            throw new EntityNotFoundException("User with ID " + user.getId() + " not found");
        }
        return userMapper.toDTO(userRepository.save(user));
    }

    @Override
    public void deleteById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User with ID " + id + " not found");
        }
        userRepository.deleteById(id);
    }
}
