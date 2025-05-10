package lab.repository;
import jakarta.persistence.LockModeType;
import lab.models.Issue;
import lab.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import java.util.Optional;


@Repository
public interface UserDao extends JpaRepository<User, Long> {

    @Lock(LockModeType.PESSIMISTIC_READ)
    boolean existsByLogin(String login);

    @Lock(LockModeType.PESSIMISTIC_READ)
    Optional<User> findById(Long aLong);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    <S extends User> S saveAndFlush(S entity);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    void deleteById(Long aLong);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    void flush();
}
