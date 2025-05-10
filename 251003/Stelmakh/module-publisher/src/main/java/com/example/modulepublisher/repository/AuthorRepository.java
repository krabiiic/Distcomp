package com.example.modulepublisher.repository;

import com.example.modulepublisher.entity.Author;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorRepository   extends JpaRepository<Author, Integer> {
    Optional<Author> findAuthorById(int id);
    Optional<Author> findAuthorByLogin(String login);
}
