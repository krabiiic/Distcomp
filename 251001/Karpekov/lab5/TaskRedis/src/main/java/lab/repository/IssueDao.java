package lab.repository;
import jakarta.persistence.LockModeType;
import lab.models.Issue;
import lab.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IssueDao extends JpaRepository<Issue, Long> {

    @Lock(LockModeType.PESSIMISTIC_READ)
    boolean existsByTitle(String title);

    @Lock(LockModeType.PESSIMISTIC_READ)
    Optional<Issue> findById(Long aLong);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    <S extends Issue> S saveAndFlush(S entity);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    void deleteById(Long aLong);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    void flush();

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Issue> deleteByUser(User user);
}
