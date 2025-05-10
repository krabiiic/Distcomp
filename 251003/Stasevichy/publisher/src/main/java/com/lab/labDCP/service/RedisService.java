package com.lab.labDCP.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;
    @Autowired
    public RedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void setValue(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public Object getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public boolean deleteValue(String key) {
        try {
            return redisTemplate.delete(key);
        } catch (Exception e) {
            System.out.println(e.toString());
            return false;
        }
    }

    public long deleteValuesByPattern(String pattern) {
        try {
            Set<String> keys = redisTemplate.keys(pattern);
            if (keys != null && !keys.isEmpty()) {
                return redisTemplate.delete(keys);
            }
            return 0L;
        } catch (Exception e) {
            System.out.println(e.toString());
            return 0;
        }
    }

    public void setValueWithExpireSeconds(String key, Object value, long seconds) {
        setValueWithExpireSeconds(key, value, seconds * 1000);
    }
}