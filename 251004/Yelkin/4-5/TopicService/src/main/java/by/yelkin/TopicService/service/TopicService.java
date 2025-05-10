package by.yelkin.TopicService.service;



import by.yelkin.TopicService.dto.topic.TopicRequestTo;
import by.yelkin.TopicService.dto.topic.TopicResponseTo;
import by.yelkin.TopicService.dto.topic.TopicUpdate;

import java.util.List;
import java.util.Optional;

public interface TopicService {
    TopicResponseTo create(TopicRequestTo topic);

    TopicResponseTo update(TopicUpdate updatedTopic);

    void deleteById(Long id);

    List<TopicResponseTo> findAll();

    Optional<TopicResponseTo> findById(Long id);
}
