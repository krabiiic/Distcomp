package by.yelkin.TopicService.service.Impl;

import by.yelkin.TopicService.dto.topic.TopicRequestTo;
import by.yelkin.TopicService.dto.topic.TopicResponseTo;
import by.yelkin.TopicService.dto.topic.TopicUpdate;
import by.yelkin.TopicService.entity.Creator;
import by.yelkin.TopicService.entity.Mark;
import by.yelkin.TopicService.entity.Topic;
import by.yelkin.TopicService.exceptionHandler.CreatorNotFoundException;
import by.yelkin.TopicService.mapper.TopicMapper;
import by.yelkin.TopicService.repository.CreatorRepository;
import by.yelkin.TopicService.repository.MarkRepository;
import by.yelkin.TopicService.repository.TopicRepository;
import by.yelkin.TopicService.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class TopicServiceImpl implements TopicService {

    private TopicRepository topicRepository;
    private CreatorRepository creatorRepository;
    private TopicMapper topicMapper;
    private MarkRepository markRepository;

    @Autowired
    public TopicServiceImpl(TopicRepository topicRepository, TopicMapper topicMapper, CreatorRepository creatorRepository, MarkRepository markRepository) {
        this.topicRepository = topicRepository;
        this.topicMapper = topicMapper;
        this.creatorRepository = creatorRepository;
        this.markRepository = markRepository;
    }
    
    @Override
    public TopicResponseTo create(TopicRequestTo topic) {
        Creator creator = creatorRepository.findById(topic.getCreatorId())
                .orElseThrow(() -> new CreatorNotFoundException(topic.getCreatorId()));

        List<Mark> tags = topic.getMarks().stream()
                .map(tagName -> markRepository.findByName(tagName)
                        .orElseGet(() -> {
                            Mark newTag = new Mark();
                            newTag.setName(tagName);
                            return markRepository.save(newTag);
                        }))
                .toList();

        Topic entity = topicMapper.toEntity(topic);
        entity.setCreator(creator);
        entity.setMarks(tags);

        return topicMapper.toResponse(topicRepository.save(entity));
    }

    @Override
    public TopicResponseTo update(TopicUpdate updatedTopic) {
        Topic topic = topicRepository.findById(updatedTopic.getId())
                .orElseThrow(() -> new IllegalArgumentException("Topic not found"));


        if (updatedTopic.getCreatorId() != null) {
            Creator creator = creatorRepository.findById(updatedTopic.getCreatorId())
                    .orElseThrow(() -> new IllegalArgumentException("Creator to update not found"));
            topic.setCreator(creator);
        }
        if (updatedTopic.getTitle() != null) {
            topic.setTitle(updatedTopic.getTitle());
        }
        if (updatedTopic.getContent() != null) {
            topic.setContent(updatedTopic.getContent());
        }
        return topicMapper.toResponse(topicRepository.save(topic));
    }

    @Override
    public void deleteById(Long id) {
        topicRepository.deleteById(id);

    }

    @Override
    public List<TopicResponseTo> findAll() {
        return StreamSupport.stream(topicRepository.findAll().spliterator(), false)
                .map(topicMapper::toResponse)
                .toList();
    }

    @Override
    public Optional<TopicResponseTo> findById(Long id) {
        return topicRepository.findById(id)
                .map(topicMapper::toResponse);
    }
}
