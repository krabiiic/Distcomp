package com.publisher.rvlab4_publisher.repository;

import com.publisher.rvlab4_publisher.entity.News;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends BaseRepository<News, Long> {}