package com.example.modulepublisher.repository.redis;

import com.example.modulepublisher.dto.MessageDTO;
import com.example.modulepublisher.dto.TweetDTO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TweetRedisRepository extends CrudRepository<TweetDTO, String> {
}
