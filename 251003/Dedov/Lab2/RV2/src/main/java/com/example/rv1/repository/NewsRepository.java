package com.example.rv1.repository;

import com.example.rv1.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NewsRepository extends JpaRepository<News, Integer> {
    Optional<News> findNewsById(int id);
    Optional<News> findNewsByTitle(String string);
}
