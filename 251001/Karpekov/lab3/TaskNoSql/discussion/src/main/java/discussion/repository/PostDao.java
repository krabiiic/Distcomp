package discussion.repository;
import discussion.models.PostKey;
import discussion.models.Post;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostDao extends CassandraRepository<Post, PostKey> {
    @Query("SELECT * FROM tbl_post WHERE id = :id LIMIT 1 ALLOW FILTERING")
    Optional<Post> findFirstById(Long id);

    @Query("DELETE FROM tbl_post WHERE id = :id ALLOW FILTERING")
    void deleteById(Long id);
}
