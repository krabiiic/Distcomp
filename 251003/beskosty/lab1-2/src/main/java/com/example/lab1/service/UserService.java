package com.example.lab1.service;

import com.example.lab1.dto.UserRequestTo;
import com.example.lab1.dto.UserResponseTo;
import com.example.lab1.exception.ForbiddenException;
import com.example.lab1.exception.NotFoundException;
import com.example.lab1.model.User;
import com.example.lab1.repository.IssueRepository;
import com.example.lab1.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final IssueRepository issueRepository;

    public UserService(UserRepository userRepository, IssueRepository issueRepository) {
        this.userRepository = userRepository;
        this.issueRepository = issueRepository;
    }

    public UserResponseTo createUser(UserRequestTo request) {
        if (userRepository.existsByLogin(request.getLogin())) {
            throw new ForbiddenException("Login already exists", 40300);
        }
        User user = new User();
        user.setLogin(request.getLogin());
        user.setPassword(request.getPassword());
        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        User saved = userRepository.save(user);
        return toDto(saved);
    }


    public List<UserResponseTo> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    
    public UserResponseTo getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found", 40401));
        return toDto(user);
    }
    
    public UserResponseTo updateUser(Long id, UserRequestTo request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found", 40401));
        user.setLogin(request.getLogin());
        user.setPassword(request.getPassword());
        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        User updated = userRepository.save(user);
        return toDto(updated);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found", 40401));
        if (issueRepository.findByUserId(id) != null) {
            issueRepository.deleteAll(issueRepository.findByUserId(id));
        }
        userRepository.delete(user);
    }
    
    private UserResponseTo toDto(User user) {
        UserResponseTo dto = new UserResponseTo();
        dto.setId(user.getId());
        dto.setLogin(user.getLogin());
        dto.setFirstname(user.getFirstname());
        dto.setLastname(user.getLastname());
        return dto;
    }
}
