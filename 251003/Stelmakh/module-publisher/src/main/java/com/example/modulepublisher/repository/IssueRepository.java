package com.example.modulepublisher.repository;

import com.example.modulepublisher.entity.Issue;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Integer> {
    Optional<Issue> findIssueById(int id);
    Optional<Issue> findIssueByTitle(String string);
}
