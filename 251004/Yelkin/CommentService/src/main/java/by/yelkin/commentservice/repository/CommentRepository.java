package by.yelkin.commentservice.repository;

import by.yelkin.commentservice.entity.Comment;
import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Long> {
    @AllowFiltering
    Optional<Comment> findById(Long id);
}
