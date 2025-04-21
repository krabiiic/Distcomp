package com.example.restservice.repository;

import com.example.restservice.model.Article;
import com.example.restservice.model.Mark;
import org.springframework.stereotype.Repository;

import java.util.Optional;


//public class ArticleRepository extends RepositoryInMemory<Article> { }
@Repository
public interface ArticleRepository extends CrudRepository<Article, Long> {
  }