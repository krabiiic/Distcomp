package com.example.restservice.controller;

import com.example.restservice.dto.request.UserRequestTo;
import com.example.restservice.dto.response.UserResponseTo;
import com.example.restservice.service.UserService;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1.0/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponseTo>> getAll() {
        return ResponseEntity.ok(userService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseTo> getById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    @PostMapping
    public ResponseEntity<UserResponseTo> create(@RequestBody UserRequestTo request) {
      return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.create(request));
    }

    @PutMapping
    public ResponseEntity<UserResponseTo> update(@RequestBody UserRequestTo request) {
        return ResponseEntity.ok(userService.update(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}