package com.example.publisher.service;

import com.example.publisher.dto.TopicRequestTo;
import com.example.publisher.dto.TopicResponseTo;
import com.example.publisher.entity.Topic;
import com.example.publisher.entity.User;
import com.example.publisher.mapper.TopicMapper;
import com.example.publisher.repository.TagRepository;
import com.example.publisher.repository.TopicRepository;
import com.example.publisher.repository.UserRepository;
import com.example.publisher.service.exception.DuplicateTitleException;
import com.example.publisher.service.exception.UserNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TopicService implements EntityService<TopicRequestTo, TopicResponseTo> {

    private final TopicRepository topicRepository;
    private final UserRepository userRepository;
    private final TopicMapper topicMapper;
    private final TagRepository tagRepository;

    @Override
    public TopicResponseTo create(TopicRequestTo entityDTO) {
        Optional<User> userOptional = userRepository.findById(entityDTO.userId());
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("User with id " + entityDTO.userId() + " not found");
        }
        try {
            Topic topic = topicMapper.toEntity(entityDTO);
            return topicMapper.toDTO(topicRepository.save(topic));
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateTitleException("Topic with title '" + entityDTO.title() + "' already exists");
        }
    }

    @Override
    public TopicResponseTo getById(Long id) {
        return topicMapper.toDTO(topicRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Topic with ID " + id + " not found")));
    }

    @Override
    public List<TopicResponseTo> getAll() {
        return topicRepository.findAll().stream()
                .map(topicMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TopicResponseTo update(TopicRequestTo entityDTO) {
        Topic topic = topicMapper.toEntity(entityDTO);
        if (!topicRepository.existsById(topic.getId())) {
            throw new EntityNotFoundException("Topic with ID " + topic.getId() + " not found");
        }
        return topicMapper.toDTO(topicRepository.save(topic));
    }

    @Override
    public void deleteById(Long id) {
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Topic not found"));

        topicRepository.delete(topic);
    }
}
