package publisher.repository;
import jakarta.persistence.LockModeType;
import publisher.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import java.util.Optional;


@Repository
public interface UserDao extends JpaRepository<User, Long> {

    boolean existsByLogin(String login);

    void flush();
}
