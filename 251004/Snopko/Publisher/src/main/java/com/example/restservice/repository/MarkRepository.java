package com.example.restservice.repository;

import com.example.restservice.model.Mark;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
//public class MarkRepository extends RepositoryInMemory<Mark> { }
public interface MarkRepository extends CrudRepository<Mark, Long> {
    Optional<Mark> findByName(String name);

}