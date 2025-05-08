package org.example.repository;

import org.example.entities.Post;
import org.example.entities.PostKey;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface PostRepository extends CassandraRepository<Post, PostKey> {
    List<Post> findByNewsId(Long newsId);
    List<Post> findById (Long id);
    void deleteByCountryAndNewsIdAndId (String country, Long newsId, Long id);
    int countByCountry (String country);
}
