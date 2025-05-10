package com.example.JPA2.Impl.Controllers;

import com.example.JPA2.Impl.DTO.UserRequestTo;
import com.example.JPA2.Impl.DTO.UserResponseTo;
import com.example.JPA2.Impl.Services.UserService;
import jakarta.validation.Valid;
import org.hibernate.sql.ast.tree.expression.Collation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1.0/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping()
    public Collection<UserResponseTo> getAll(){
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseTo> getUser(@PathVariable long id) {
        try{
            UserResponseTo user = userService.get(id);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (NoSuchElementException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping()
    public ResponseEntity<UserResponseTo> createUser(@Valid @RequestBody UserRequestTo UserTo) {
        UserResponseTo addedUser = userService.add(UserTo);
        return new ResponseEntity<>(addedUser, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponseTo> deleteUser(@PathVariable long id) {
        UserResponseTo deletedUser = userService.delete(id);
        return new ResponseEntity<>(deletedUser, deletedUser == null ? HttpStatus.NOT_FOUND : HttpStatus.NO_CONTENT);
    }

    @PutMapping()
    public ResponseEntity<UserResponseTo> updateUser(@Valid @RequestBody UserRequestTo userRequestTo) {
        UserResponseTo userResponseTo = userService.update(userRequestTo);
        return new ResponseEntity<>(userResponseTo, userResponseTo.getFirstname() == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
    }
}