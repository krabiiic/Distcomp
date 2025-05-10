package by.yelkin.CommentService.repository;

import by.yelkin.CommentService.entity.Comment;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CommentRepository extends CassandraRepository<Comment, String> {
}
