package com.example.restservice.service;

import com.example.restservice.dto.request.UserRequestTo;
import com.example.restservice.dto.response.UserResponseTo;
import com.example.restservice.model.User;
import com.example.restservice.repository.UserRepository;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public List<UserResponseTo> getAll() {
        return userRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public UserResponseTo getById(Long id) {
        return userRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
    }

    public UserResponseTo create(UserRequestTo request) {
        User user = modelMapper.map(request, User.class);
        User saved = userRepository.save(user);
        return toResponse(saved);
    }

    public UserResponseTo update(UserRequestTo request) {
        if (request.getId() == null) {
            throw new NoSuchElementException("User not found");
        }

        User existing = userRepository.findById(request.getId())
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        modelMapper.map(request, existing);
        User updated = userRepository.save(existing);
        return toResponse(updated);
    }

    public void delete(Long id) {
        if (userRepository.findById(id).isEmpty()) {
            throw new NoSuchElementException("User not found");
        }
        userRepository.deleteById(id);
    }

    private UserResponseTo toResponse(User user) {
        return modelMapper.map(user, UserResponseTo.class);
    }
}
