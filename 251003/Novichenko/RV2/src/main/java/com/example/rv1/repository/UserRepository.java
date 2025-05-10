package com.example.rv1.repository;

import com.example.rv1.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findUserById(int id);
    Optional<User> findUserByLogin(String login);
}
