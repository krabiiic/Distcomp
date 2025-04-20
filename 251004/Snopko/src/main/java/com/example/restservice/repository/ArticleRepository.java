package com.example.restservice.repository;

import com.example.restservice.model.Article;
import com.example.restservice.model.User;
import org.springframework.stereotype.Repository;

@Repository
public class ArticleRepository extends RepositoryInMemory<Article> { }
