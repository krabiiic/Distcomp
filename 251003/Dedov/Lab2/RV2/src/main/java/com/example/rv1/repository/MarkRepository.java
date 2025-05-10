package com.example.rv1.repository;

import com.example.rv1.entity.Mark;
import com.example.rv1.entity.Editor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MarkRepository extends JpaRepository<Mark, Integer> {
    Optional<Mark> findMarkById(int id);
}
