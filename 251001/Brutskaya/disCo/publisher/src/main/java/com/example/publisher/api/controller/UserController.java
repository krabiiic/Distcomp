package com.example.publisher.api.controller;

import com.example.publisher.api.dto.request.UserRequestTo;
import com.example.publisher.api.dto.responce.UserResponseTo;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

public interface UserController {

    @PostMapping("/api/v1.0/users")
    @ResponseStatus(HttpStatus.CREATED)
    UserResponseTo create(@RequestBody
                          @Valid UserRequestTo request);

    @GetMapping("/api/v1.0/users")
    @ResponseStatus(HttpStatus.OK)
    List<UserResponseTo> getAll();

    @GetMapping("/api/v1.0/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    UserResponseTo getById(@PathVariable Long id);

    @PutMapping("/api/v1.0/users")
    @ResponseStatus(HttpStatus.OK)
    UserResponseTo update(@RequestBody
                          @Valid UserRequestTo request);

    @DeleteMapping("/api/v1.0/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable Long id);
}
