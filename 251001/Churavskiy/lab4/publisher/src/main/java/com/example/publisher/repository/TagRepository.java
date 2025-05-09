package com.example.publisher.repository;

import com.example.publisher.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByName(String name);

    @Query("SELECT COUNT(t) FROM Topic t JOIN t.tags tag WHERE tag.id = :tagId")
    Long countTopicsByTagId(@Param("tagId") Long tagId);
}
