package com.example.publisher.repository;

import com.example.publisher.model.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> {
    boolean existsByTitle(String title);
}
