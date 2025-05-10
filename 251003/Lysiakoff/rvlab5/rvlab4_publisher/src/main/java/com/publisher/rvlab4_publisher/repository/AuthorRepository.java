package com.publisher.rvlab4_publisher.repository;

import com.publisher.rvlab4_publisher.entity.Author;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorRepository extends BaseRepository<Author, Long> {

    Optional<Author> findByLogin(String login);

}

