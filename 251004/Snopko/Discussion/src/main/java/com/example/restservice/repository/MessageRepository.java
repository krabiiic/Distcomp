package com.example.restservice.repository;

import com.example.restservice.model.Message;
import org.springframework.stereotype.Repository;
import org.springframework.data.cassandra.repository.CassandraRepository;

@Repository
public interface MessageRepository extends CassandraRepository<Message, String> {
}