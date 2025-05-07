package by.kapinskiy.Publisher.repositories;

import by.kapinskiy.Publisher.models.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface IssuesRepository extends JpaRepository<Issue, Long> {
    boolean existsByTitle(String title);
}
