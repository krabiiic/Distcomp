package com.dc.repository;

import com.dc.model.blo.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    boolean existsByLogin(String login);
}
