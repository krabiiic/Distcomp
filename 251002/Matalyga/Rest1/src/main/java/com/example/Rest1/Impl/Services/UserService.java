package com.example.Rest1.Impl.Services;

import com.example.Rest1.Impl.Entity.User;
import com.example.Rest1.Impl.DTO.UserRequestTo;
import com.example.Rest1.Impl.DTO.UserResponseTo;
import com.example.Rest1.Impl.CRUD.UserCRUD;
import com.example.Rest1.Mappers.UserMapper;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserService implements Service<UserResponseTo, UserRequestTo> {

    @Autowired
    private UserCRUD userCRUD;

    public UserService() { }

    public List<UserResponseTo> getAll() {
        List<User> userList = userCRUD.getAll();
        List<UserResponseTo> resultList = new ArrayList<>();
        for (int i = 0; i < userList.size(); i++) {
            resultList.add(UserMapper.INSTANCE.UserToUserResponseTo(userList.get(i)));
        }
        return resultList;
    }

    public UserResponseTo update(@Valid UserRequestTo updatingUser) {
        User user = UserMapper.INSTANCE.UserRequestToToUser(updatingUser);
        boolean result = userCRUD.update(user);
        UserResponseTo responseTo = result ? UserMapper.INSTANCE.UserToUserResponseTo(user) : null;
        return responseTo;
    }

    public UserResponseTo get(@Min(0) long id) {
        return UserMapper.INSTANCE.UserToUserResponseTo(userCRUD.get(id));
    }

    public UserResponseTo delete(@Min(0) long id) {
        return UserMapper.INSTANCE.UserToUserResponseTo(userCRUD.delete(id));
    }

    public UserResponseTo add(@Valid UserRequestTo userRequestTo) {
        User user = UserMapper.INSTANCE.UserRequestToToUser(userRequestTo);
        return UserMapper.INSTANCE.UserToUserResponseTo(userCRUD.insert(user));
    }

}