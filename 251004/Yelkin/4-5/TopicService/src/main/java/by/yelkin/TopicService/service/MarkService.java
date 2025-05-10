package by.yelkin.TopicService.service;

import by.yelkin.TopicService.dto.mark.MarkRequestTo;
import by.yelkin.TopicService.dto.mark.MarkResponseTo;
import by.yelkin.TopicService.dto.mark.MarkUpdate;

import java.util.List;
import java.util.Optional;

public interface MarkService {
    MarkResponseTo create(MarkRequestTo tag);

    MarkResponseTo update(MarkUpdate updatedTag);

    void deleteById(Long id);

    List<MarkResponseTo> findAll();

    Optional<MarkResponseTo> findById(Long id);
}