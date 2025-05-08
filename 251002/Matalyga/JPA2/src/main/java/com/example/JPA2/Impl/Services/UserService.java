package com.example.JPA2.Impl.Services;

import com.example.JPA2.Impl.CRUD.UserCRUD;
import com.example.JPA2.Impl.DTO.UserRequestTo;
import com.example.JPA2.Impl.DTO.UserResponseTo;
import com.example.JPA2.Impl.Entity.User;
import com.example.JPA2.Impl.Repository.UserRepository;
import com.example.JPA2.Mappers.UserMapper;
import com.example.JPA2.Mappers.UserListMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.annotation.Validated;
import com.example.JPA2.Exceptions.Violation;
import com.example.JPA2.Exceptions.NotFoundException;
import java.util.List;

@org.springframework.stereotype.Service
@Validated
public class UserService implements Service<UserResponseTo, UserRequestTo> {

    @Autowired
    private UserCRUD userCRUD;

    @Autowired
    private UserListMapper userListMapper;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRepository userRepository;

    public UserService() { }

    public List<UserResponseTo> getAll() {
        return userRepository.getAll()
                .map(userMapper::UserToUserResponseTo)
                .toList();
    }

    public UserResponseTo update(UserRequestTo updatingUser) {
        if(userRepository.existsById(updatingUser.getId())) {
            User newUser = userMapper.UserRequestToToUser(updatingUser);
            newUser.setId(updatingUser.getId());

            return userMapper.UserToUserResponseTo(userRepository.save(newUser));
        }
        throw new NotFoundException("User not found", 404);
    }

    public UserResponseTo get(long id) {
        return userRepository.get(id)
                .map(userMapper::UserToUserResponseTo)
                .orElse(null);
    }

    public UserResponseTo delete(long id) {
       User user = userRepository.findById(id)
               .orElseThrow(() -> new NotFoundException("User not found", 404));
       userRepository.delete(user);
       return userMapper.UserToUserResponseTo(user);
    }

    public UserResponseTo add(UserRequestTo userRequestTo) {
        try {
            return userRepository.add(userMapper.UserRequestToToUser(userRequestTo))
                    .map(userMapper::UserToUserResponseTo)
                    .orElseThrow();

        } catch (DataIntegrityViolationException e) {
            throw new Violation("Login is already taken");
        }
    }
}