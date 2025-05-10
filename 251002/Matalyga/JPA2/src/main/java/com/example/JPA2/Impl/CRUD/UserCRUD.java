package com.example.JPA2.Impl.CRUD;

import com.example.JPA2.Impl.Entity.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserCRUD implements CRUD<User> {
    private final Map<Long, User> UserMemory = new HashMap<>();

    @Override
    public User get(long id) {
        User user = UserMemory.get(id);
        if (user != null) {
            user.setId(id);
        }
        return user;
    }

    @Override
    public List<User> getAll() {
        List<User> userList = new ArrayList<>();
        for (Long key : UserMemory.keySet()) {
            User user = UserMemory.get(key);
            user.setId(key);
            userList.add(user);
        }
        return userList;
    }

    @Override
    public User delete(long id) {
        return UserMemory.remove(id);
    }

    @Override
    public User insert(User insertObject) {
        UserMemory.put(insertObject.getId(), insertObject);
        return insertObject;
    }

    @Override
    public boolean update(User updatingValue) {
        return UserMemory.replace(updatingValue.getId(), UserMemory.get(updatingValue.getId()), updatingValue);
    }
}