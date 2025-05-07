package com.example.rv1.repository;

import com.example.rv1.entity.Tweet;
import com.example.rv1.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TweetRepository extends JpaRepository<Tweet, Integer> {
    Optional<Tweet> findTweetById(int id);
    Optional<Tweet> findTweetByTitle(String string);
}
