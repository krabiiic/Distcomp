package com.example.restservice.repository;

import com.example.restservice.model.Message;
import org.springframework.stereotype.Repository;

@Repository
//public class MessageRepository extends RepositoryInMemory<Message> { }
public interface MessageRepository extends CrudRepository<Message, Long> {
}