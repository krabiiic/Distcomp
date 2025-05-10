package lab.repository;
import jakarta.persistence.LockModeType;
import lab.models.Issue;
import lab.models.Post;
import lab.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostDao extends JpaRepository<Post, Long> {

    @Lock(LockModeType.PESSIMISTIC_READ)
    Optional<Post> findById(Long aLong);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    <S extends Post> S saveAndFlush(S entity);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    void deleteById(Long aLong);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    void flush();

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Post> deleteByIssue(Issue issue);
}
