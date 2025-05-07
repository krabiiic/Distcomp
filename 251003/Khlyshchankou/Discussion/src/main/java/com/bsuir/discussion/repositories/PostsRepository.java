package com.bsuir.discussion.repositories;

import com.bsuir.discussion.models.Post;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostsRepository extends CrudRepository<Post, Post.PostKey> {
    @Query("SELECT * FROM tbl_post WHERE country = :country AND id = :id")
    Optional<Post> findByCountryAndId(@Param("country") String country, @Param("id") Long id);

    @Query("DELETE FROM tbl_post WHERE country = :country AND id = :id")
    void deleteByCountryAndId(@Param("country") String country, @Param("id") Long id);
}
