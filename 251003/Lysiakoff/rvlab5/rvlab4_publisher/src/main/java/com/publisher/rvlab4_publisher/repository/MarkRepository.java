package com.publisher.rvlab4_publisher.repository;

import com.publisher.rvlab4_publisher.entity.Mark;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MarkRepository extends BaseRepository<Mark, Long> {
    Optional<Mark> findByName(String name);
}