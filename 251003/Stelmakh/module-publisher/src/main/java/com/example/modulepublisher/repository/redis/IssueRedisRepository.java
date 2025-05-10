package com.example.modulepublisher.repository.redis;

import com.example.modulepublisher.dto.IssueDTO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueRedisRepository extends CrudRepository<IssueDTO, String> {
}
