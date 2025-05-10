package com.example.modulepublisher.repository;

import com.example.modulepublisher.entity.Message;

import org.springframework.stereotype.Repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
@Repository
public interface MessageRepository  extends JpaRepository<Message, Integer> {
    Optional<Message> findUserById(int id);
    Message save(Message message);
}
