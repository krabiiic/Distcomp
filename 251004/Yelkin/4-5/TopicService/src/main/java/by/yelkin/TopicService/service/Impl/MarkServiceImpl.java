package by.yelkin.TopicService.service.Impl;

import by.yelkin.TopicService.dto.mark.MarkRequestTo;
import by.yelkin.TopicService.dto.mark.MarkResponseTo;
import by.yelkin.TopicService.dto.mark.MarkUpdate;
import by.yelkin.TopicService.entity.Mark;
import by.yelkin.TopicService.mapper.MarkMapper;
import by.yelkin.TopicService.repository.MarkRepository;
import by.yelkin.TopicService.service.MarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class MarkServiceImpl implements MarkService {

    private MarkRepository markRepository;
    private MarkMapper markMapper;

    @Autowired
    public MarkServiceImpl(MarkRepository markRepository, MarkMapper markMapper) {
        this.markRepository = markRepository;
        this.markMapper = markMapper;
    }

    @Override
    public MarkResponseTo create(MarkRequestTo tag) {
        return markMapper.toResponse(markRepository.save(markMapper.toEntity(tag)));

    }

    @Override
    public MarkResponseTo update(MarkUpdate updatedTag) {
        Mark tag = markRepository.findById(updatedTag.getId())
                .orElseThrow(() -> new IllegalArgumentException("Tag not found"));

        if (updatedTag.getId() != null) {
            tag.setId(updatedTag.getId());
        }
        if (updatedTag.getName() != null) {
            tag.setName(updatedTag.getName());
        }

        return markMapper.toResponse(markRepository.save(tag));
    }

    @Override
    public void deleteById(Long id) {
        markRepository.deleteById(id);
    }

    @Override
    public List<MarkResponseTo> findAll() {
        return StreamSupport.stream(markRepository.findAll().spliterator(), false)
                .map(markMapper::toResponse)
                .toList();
    }

    @Override
    public Optional<MarkResponseTo> findById(Long id) {
        return markRepository.findById(id)
                .map(markMapper::toResponse);
    }
}
