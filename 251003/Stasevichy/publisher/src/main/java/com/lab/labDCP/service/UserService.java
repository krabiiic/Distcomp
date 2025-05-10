package com.lab.labDCP.service;

import com.lab.labDCP.dto.UserRequestTo;
import com.lab.labDCP.dto.UserResponseTo;
import com.lab.labDCP.entity.User;
import com.lab.labDCP.repository.UserRepositoryJPA;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepositoryJPA userRepositoryJPA;

    public List<UserResponseTo> getAllUsers() {
        return userRepositoryJPA.findAll().stream()
                .map(user -> new UserResponseTo(
                        user.getId(),
                        user.getLogin(),
                        user.getFirstname(),
                        user.getLastname(),
                        user.getStoryIds()
                ))
                .collect(Collectors.toList());
    }

    public UserResponseTo getUserById(Long id) {
        return userRepositoryJPA.findById(id)
                .map(user -> new UserResponseTo(
                        id,
                        user.getLogin(),
                        user.getFirstname(),
                        user.getLastname(),
                        user.getStoryIds()
                ))
                .orElseThrow(() -> new NoSuchElementException("User not found"));
    }

    @Transactional
    public UserResponseTo createUser(User user) {
        userRepositoryJPA.save(user);
        return new UserResponseTo(
                user.getId(),
                user.getLogin(),
                user.getFirstname(),
                user.getLastname(),
                user.getStoryIds()
        );
    }

    public UserResponseTo updateUser(Long id, UserRequestTo request) {
        User user = userRepositoryJPA.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + id));

        if (request.getLogin() == null || request.getLogin().length() < 3) {
            throw new IllegalArgumentException("Login must be at least 3 characters long");
        }

        user.setLogin(request.getLogin());
        user.setPassword(request.getPassword());
        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());

        userRepositoryJPA.save(user);
        return new UserResponseTo(
                id,
                user.getLogin(),
                user.getFirstname(),
                user.getLastname(),
                user.getStoryIds()
        );
    }

    public void deleteUser(Long id) {
        User user = userRepositoryJPA.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        userRepositoryJPA.deleteById(id);
    }
}
