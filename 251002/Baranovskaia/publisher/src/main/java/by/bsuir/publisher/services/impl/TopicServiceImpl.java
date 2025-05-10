package by.bsuir.publisher.services.impl;

import by.bsuir.publisher.domain.Topic;
import by.bsuir.publisher.dto.requests.TopicRequestDto;
import by.bsuir.publisher.dto.requests.converters.TopicRequestConverter;
import by.bsuir.publisher.dto.responses.TopicResponseDto;
import by.bsuir.publisher.dto.responses.converters.CollectionTopicResponseConverter;
import by.bsuir.publisher.dto.responses.converters.TopicResponseConverter;
import by.bsuir.publisher.exceptions.EntityExistsException;
import by.bsuir.publisher.exceptions.Messages;
import by.bsuir.publisher.exceptions.NoEntityExistsException;
import by.bsuir.publisher.repositories.TopicRepository;
import by.bsuir.publisher.services.TopicService;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Validated
public class TopicServiceImpl implements TopicService {

    private final TopicRepository topicRepository;
    private final TopicRequestConverter topicRequestConverter;
    private final TopicResponseConverter topicResponseConverter;
    private final CollectionTopicResponseConverter collectionTopicResponseConverter;

    @Override
    @Validated
    public TopicResponseDto create(@Valid @NonNull TopicRequestDto dto) throws EntityExistsException {
        Optional<Topic> topic = dto.getId() == null ? Optional.empty() : topicRepository.findById(dto.getId());
        if (topic.isEmpty()) {
            return topicResponseConverter.toDto(topicRepository.save(topicRequestConverter.fromDto(dto)));
        } else {
            throw new EntityExistsException(Messages.EntityExistsException);
        }
    }

    @Override
    public Optional<TopicResponseDto> read(@NonNull Long id) {
        return topicRepository.findById(id).flatMap(topic -> Optional.of(
                topicResponseConverter.toDto(topic)));
    }

    @Override
    @Validated
    public TopicResponseDto update(@Valid @NonNull TopicRequestDto dto) throws NoEntityExistsException {
        Optional<Topic> topic = dto.getId() == null || topicRepository.findById(dto.getId()).isEmpty() ?
                Optional.empty() : Optional.of(topicRequestConverter.fromDto(dto));
        return topicResponseConverter.toDto(topicRepository.save(topic.orElseThrow(() ->
                new NoEntityExistsException(Messages.NoEntityExistsException))));
    }

    @Override
    public Long delete(@NonNull Long id) throws NoEntityExistsException {
        Optional<Topic> topic = topicRepository.findById(id);
        topicRepository.deleteById(topic.map(Topic::getId).orElseThrow(() ->
                new NoEntityExistsException(Messages.NoEntityExistsException)));
        return topic.get().getId();
    }

    @Override
    public List<TopicResponseDto> readAll() {
        return collectionTopicResponseConverter.toListDto(topicRepository.findAll());
    }
}
