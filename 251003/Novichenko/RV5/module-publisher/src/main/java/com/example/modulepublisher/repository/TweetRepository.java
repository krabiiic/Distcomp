package com.example.modulepublisher.repository;

import com.example.modulepublisher.entity.Tweet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
@Repository
public interface TweetRepository extends JpaRepository<Tweet, Integer> {
    Optional<Tweet> findTweetById(int id);
    Optional<Tweet> findTweetByTitle(String string);
}
