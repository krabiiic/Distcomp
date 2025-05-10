package com.example.JPA2.Impl.Repository;

import com.example.JPA2.Impl.Entity.News;

public interface NewsRepository extends com.example.JPA2.Impl.Repository.Repository<News>{
    boolean existsByTitle(String title);
}
