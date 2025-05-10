package com.example.lab1.controller;

import com.example.lab1.dto.UserRequestTo;
import com.example.lab1.dto.UserResponseTo;
import com.example.lab1.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1.0/users")
public class UserController {

    private final UserService userService;
    
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    @PostMapping
    public ResponseEntity<UserResponseTo> createUser(@Valid @RequestBody UserRequestTo request) {
        UserResponseTo response = userService.createUser(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @GetMapping
    public ResponseEntity<List<UserResponseTo>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseTo> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }
    
    @PutMapping
    public ResponseEntity<UserResponseTo> updateUser(@Valid @RequestBody UserRequestTo request) {
        if(request.getId() == null) {
            throw new IllegalArgumentException("ID must be provided");
        }
        return ResponseEntity.ok(userService.updateUser(request.getId(), request));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
