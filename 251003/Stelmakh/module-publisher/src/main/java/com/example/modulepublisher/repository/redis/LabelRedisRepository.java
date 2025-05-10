package com.example.modulepublisher.repository.redis;

import com.example.modulepublisher.dto.LabelDTO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LabelRedisRepository extends CrudRepository<LabelDTO, String> {
}
