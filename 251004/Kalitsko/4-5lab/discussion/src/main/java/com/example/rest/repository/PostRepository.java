package com.example.rest.repository;

import com.example.rest.entity.Post;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends CassandraRepository<Post, String> {
}
