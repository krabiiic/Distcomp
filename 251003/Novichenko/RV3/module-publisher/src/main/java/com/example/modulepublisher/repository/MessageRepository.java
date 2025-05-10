package com.example.modulepublisher.repository;

import com.example.modulepublisher.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    Optional<Message> findUserById(int id);
}
