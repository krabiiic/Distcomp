package by.zdanovich.Publisher.repositories;

import by.zdanovich.Publisher.models.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> {
    boolean existsByTitle(String title);
}
