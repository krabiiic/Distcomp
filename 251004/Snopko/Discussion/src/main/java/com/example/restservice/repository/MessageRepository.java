package com.example.restservice.repository;

import com.example.restservice.model.Message;
import org.springframework.stereotype.Repository;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.Optional;

@Repository
public interface MessageRepository extends CassandraRepository<Message, String> {
    @Override
    Optional<Message> findById(String s);
}