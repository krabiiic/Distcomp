package org.example.Impl.Repository;

import org.example.Impl.Entity.News;

public interface NewsRepository extends Repository<News>{
    boolean existsByTitle(String title);
}
