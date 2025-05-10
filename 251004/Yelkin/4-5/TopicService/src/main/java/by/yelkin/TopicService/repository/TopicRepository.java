package by.yelkin.TopicService.repository;

import by.yelkin.TopicService.entity.Topic;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicRepository extends CrudRepository<Topic, Long> {

}
