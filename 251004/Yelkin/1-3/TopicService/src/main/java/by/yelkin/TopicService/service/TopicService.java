package by.yelkin.TopicService.service;

import by.yelkin.api.topic.dto.TopicRq;
import by.yelkin.api.topic.dto.TopicRs;
import by.yelkin.api.topic.dto.TopicUpdateRq;
import by.yelkin.TopicService.mapping.TopicMapper;
import by.yelkin.TopicService.repository.CreatorRepository;
import by.yelkin.TopicService.repository.TopicRepository;
import by.yelkin.apihandler.exception.ApiError;
import by.yelkin.apihandler.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TopicService {
    private final TopicRepository topicRepository;
    private final CreatorRepository creatorRepository;
    private final TopicMapper topicMapper;

    @Transactional
    public TopicRs create(TopicRq rq) {
        if (topicRepository.findByTitle(rq.getTitle()).isPresent()) {
            throw new ApiException(ApiError.ERR_NO_RIGHTS);
        }
        if (creatorRepository.findById(rq.getCreatorId()).isEmpty()) {
            throw new ApiException(ApiError.ERR_CREATOR_NOT_FOUND, rq.getCreatorId().toString());
        }
        return topicMapper.toDto(topicRepository.save(topicMapper.fromDto(rq)));
    }

    @Transactional
    public TopicRs readById(Long id) {
        return topicMapper.toDto(topicRepository.findById(id)
                .orElseThrow(() -> new ApiException(ApiError.ERR_TOPIC_NOT_FOUND, id.toString())));
    }

    @Transactional
    public List<TopicRs> readAll() {
        return topicMapper.toDtoList(topicRepository.findAll());
    }

    @Transactional
    public TopicRs update(TopicUpdateRq rq) {
        var topic = topicRepository.findById(rq.getId())
                .orElseThrow(() -> new ApiException(ApiError.ERR_TOPIC_NOT_FOUND, rq.getId().toString()));

        topicMapper.updateTopic(topic, rq);

        return topicMapper.toDto(topicRepository.save(topic));
    }

    @Transactional
    public void deleteById(Long id) {
        if (topicRepository.findById(id).isEmpty()) {
            throw new ApiException(ApiError.ERR_TOPIC_NOT_FOUND, id.toString());
        }
        topicRepository.deleteById(id);
    }
}
