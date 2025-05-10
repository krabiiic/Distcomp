package com.example.lab1.service;

import com.example.lab1.dto.ReactionResponseTo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisCacheService {

    private static final Logger logger = LoggerFactory.getLogger(RedisCacheService.class);
    private final RedisTemplate<String, ReactionResponseTo> redisTemplate;
    
    private static final String REACTION_KEY_PREFIX = "reaction:";
    
    public RedisCacheService(RedisTemplate<String, ReactionResponseTo> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    
    public void saveReaction(ReactionResponseTo reaction) {
        try {
            String key = REACTION_KEY_PREFIX + reaction.getId();
            redisTemplate.opsForValue().set(key, reaction);
            redisTemplate.expire(key, 1, TimeUnit.HOURS);
            logger.info("Cached reaction with ID: {}", reaction.getId());
        } catch (Exception e) {
            logger.error("Error saving to Redis cache: {}", e.getMessage());
        }
    }
    
    public ReactionResponseTo getReaction(Long id) {
        try {
            String key = REACTION_KEY_PREFIX + id;
            ReactionResponseTo cachedReaction = redisTemplate.opsForValue().get(key);
            if (cachedReaction != null) {
                logger.info("Cache hit for reaction with ID: {}", id);
                return cachedReaction;
            }
            logger.info("Cache miss for reaction with ID: {}", id);
            return null;
        } catch (Exception e) {
            logger.error("Error accessing Redis cache: {}", e.getMessage());
            return null;
        }
    }
    
    public void deleteReaction(Long id) {
        try {
            String key = REACTION_KEY_PREFIX + id;
            redisTemplate.delete(key);
            logger.info("Removed reaction with ID: {} from cache", id);
        } catch (Exception e) {
            logger.error("Error deleting from Redis cache: {}", e.getMessage());
        }
    }
}