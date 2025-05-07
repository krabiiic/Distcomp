package com.example.discussion.repository;

import com.example.discussion.model.News;
import org.springframework.data.jpa.repository.JpaRepository;

//@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
    boolean existsByTitle(String title);
}