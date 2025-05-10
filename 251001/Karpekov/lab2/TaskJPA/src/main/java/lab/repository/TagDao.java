package lab.repository;
import jakarta.persistence.LockModeType;
import lab.models.Issue;
import lab.models.Tag;
import lab.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface TagDao extends JpaRepository<Tag, Long> {

    @Lock(LockModeType.PESSIMISTIC_READ)
    Optional<Tag> findById(Long aLong);

    @Lock(LockModeType.PESSIMISTIC_READ)
    Optional<Tag> findByName(String name);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    <S extends Tag> S saveAndFlush(S entity);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    void deleteById(Long aLong);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    void flush();
}
