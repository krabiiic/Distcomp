package com.example.modulediscussion.repository;

import com.example.modulediscussion.entity.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MessageRepository extends MongoRepository<Message, Integer> {
    Optional<Message> findUserById(int id);
}
