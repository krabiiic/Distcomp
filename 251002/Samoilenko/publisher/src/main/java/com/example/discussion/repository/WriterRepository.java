package com.example.discussion.repository;

import com.example.discussion.model.Writer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//@Repository
public interface WriterRepository extends JpaRepository<Writer, Long> {
    Optional<Writer> findByLogin(String login);
}