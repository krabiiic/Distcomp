package by.bsuir.publisher.repositories;

import by.bsuir.publisher.domain.Creator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreatorRepository extends JpaRepository<Creator, Long> {
}
