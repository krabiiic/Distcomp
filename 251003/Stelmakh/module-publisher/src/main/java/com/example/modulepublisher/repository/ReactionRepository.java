package com.example.modulepublisher.repository;

import com.example.modulepublisher.entity.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ReactionRepository  extends JpaRepository<Reaction, Integer> {
    Optional<Reaction> findAuthorById(int id);
    Reaction save(Reaction message);
}
