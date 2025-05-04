package com.example.modulepublisher.repository.redis;

import com.example.modulepublisher.dto.ReactionDTO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReactionRedisRepository extends CrudRepository<ReactionDTO, String> {

}
