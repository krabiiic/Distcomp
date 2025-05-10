package com.example.publisher.api.controller.impl;

import com.example.publisher.api.controller.UserController;
import com.example.publisher.api.dto.request.UserRequestTo;
import com.example.publisher.api.dto.responce.UserResponseTo;
import com.example.publisher.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserControllerImpl implements UserController {

    private final UserService userService;

    @Autowired
    public UserControllerImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserResponseTo create(UserRequestTo request) {
        return userService.create(request);
    }

    @Override
    public List<UserResponseTo> getAll() {
        return userService.getAll();
    }

    @Override
    public UserResponseTo getById(Long id) {
        return userService.getById(id);
    }

    @Override
    public UserResponseTo update(UserRequestTo request) {
        return userService.update(request);
    }

    @Override
    public void delete(Long id) {
        userService.delete(id);
    }
}
