package com.example.lab1.repository;

import com.example.lab1.model.Issue;
import com.example.lab1.model.IssueMark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IssueMarkRepository extends JpaRepository<IssueMark, Long> {
    List<IssueMark> findByIssue(Issue issue);
    List<IssueMark> findByIssue(Optional<Issue> byId);
}
