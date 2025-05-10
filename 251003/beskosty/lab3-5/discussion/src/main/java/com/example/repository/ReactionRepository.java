package com.example.repository;

import com.example.model.Reaction;
import com.example.model.Reaction.ReactionKey;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReactionRepository extends CassandraRepository<Reaction, ReactionKey> {
}
