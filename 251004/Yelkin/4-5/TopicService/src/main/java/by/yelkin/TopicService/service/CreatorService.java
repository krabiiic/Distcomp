package by.yelkin.TopicService.service;

import by.yelkin.TopicService.dto.creator.CreatorRequestTo;
import by.yelkin.TopicService.dto.creator.CreatorResponseTo;
import by.yelkin.TopicService.dto.creator.CreatorUpdate;

import java.util.List;
import java.util.Optional;

public interface CreatorService {
    CreatorResponseTo create(CreatorRequestTo creator);
    CreatorResponseTo update(CreatorUpdate updatedCreator);
    void deleteById(Long id);
    List<CreatorResponseTo> findAll();
    Optional<CreatorResponseTo> findById(Long id);

}
