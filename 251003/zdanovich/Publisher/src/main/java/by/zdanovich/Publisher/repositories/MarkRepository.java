package by.zdanovich.Publisher.repositories;

import by.zdanovich.Publisher.models.Mark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarkRepository extends JpaRepository<Mark, Long> {
    boolean existsByName(String name);
}
