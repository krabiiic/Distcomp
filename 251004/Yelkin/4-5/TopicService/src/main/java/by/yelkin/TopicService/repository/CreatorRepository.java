package by.yelkin.TopicService.repository;

import by.yelkin.TopicService.entity.Creator;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreatorRepository extends CrudRepository<Creator, Long> {
}
