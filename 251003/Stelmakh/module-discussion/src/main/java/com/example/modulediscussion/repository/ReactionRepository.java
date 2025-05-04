package com.example.modulediscussion.repository;

import com.example.modulediscussion.entity.Reaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReactionRepository extends MongoRepository<Reaction, Integer> {
    Optional<Reaction> findAuthorById(int id);
}
