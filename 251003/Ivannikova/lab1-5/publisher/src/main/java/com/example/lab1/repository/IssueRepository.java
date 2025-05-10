package com.example.lab1.repository;

import com.example.lab1.model.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> {
    List<Issue> findByUserId(Long id);
    boolean existsByTitle(String title);
}
