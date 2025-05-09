package by.yelkin.TopicService.repository;

import by.yelkin.TopicService.entity.Mark;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface MarkRepository extends CrudRepository<Mark, Long> {

    Optional<Mark> findByName(String name);
}
