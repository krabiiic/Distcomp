package org.example.Impl.Services;

import org.example.Exceptions.NotFoundException;
import org.example.Exceptions.Violation;
import org.example.Impl.DTO.UserRequestTo;
import org.example.Impl.DTO.UserResponseTo;
import org.example.Impl.Entity.User;
import org.example.Impl.Repository.UserRepository;
import org.example.Mappers.UserListMapper;
import org.example.Mappers.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@org.springframework.stereotype.Service
@Validated
public class UserService implements Service<UserResponseTo, UserRequestTo> {

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