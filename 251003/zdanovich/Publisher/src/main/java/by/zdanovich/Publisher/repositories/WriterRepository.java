package by.zdanovich.Publisher.repositories;

import by.zdanovich.Publisher.models.Writer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WriterRepository extends JpaRepository<Writer, Long> {
    boolean existsByLogin(String login);
}
