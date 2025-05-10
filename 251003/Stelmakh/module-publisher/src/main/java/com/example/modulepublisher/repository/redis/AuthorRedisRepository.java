package com.example.modulepublisher.repository.redis;

import com.example.modulepublisher.dto.AuthorDTO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRedisRepository extends CrudRepository<AuthorDTO, String> {
}
