package by.ryabchikov.tweet_service.repository;

import by.ryabchikov.tweet_service.entity.Creator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CreatorRepository extends JpaRepository<Creator, Long> {
    Optional<Creator> findByLogin(String login);
}
