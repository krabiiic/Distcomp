package com.example.modulepublisher.repository;

import com.example.modulepublisher.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
@Repository
public interface UserRepository   extends JpaRepository<User, Integer> {
    Optional<User> findUserById(int id);
    Optional<User> findUserByLogin(String login);
}
