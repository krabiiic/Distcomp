package by.kardychka.Publisher.repositories;

import by.kardychka.Publisher.models.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface NewssRepository extends JpaRepository<News, Long> {
    boolean existsByTitle(String title);
}
