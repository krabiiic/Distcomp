package com.example.discussion.repository;

import com.example.discussion.entity.Reaction;
import com.example.discussion.entity.ReactionKey;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReactionRepository extends CassandraRepository<Reaction, ReactionKey> {
    @Query("SELECT * FROM tbl_notes WHERE id = :id ALLOW FILTERING")
    Optional<Reaction> findFirstById(Long id);

    @Query("DELETE FROM tbl_notes WHERE id = :id ALLOW FILTERING")
    void deleteById(Long id);
}
