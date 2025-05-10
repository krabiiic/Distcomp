package com.example.repository;

import com.example.model.Post;
import com.example.model.Post.PostKey;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends CassandraRepository<Post, PostKey> {
}
