package com.example.publisher.service;

import com.example.publisher.dto.TagRequestTo;
import com.example.publisher.dto.TagResponseTo;
import com.example.publisher.entity.Tag;
import com.example.publisher.mapper.TagMapper;
import com.example.publisher.repository.TagRepository;
import com.example.publisher.service.EntityService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagService implements EntityService<TagRequestTo, TagResponseTo> {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper = TagMapper.INSTANCE;

    @Override
    public TagResponseTo create(TagRequestTo entityDTO) {
        Tag tag = tagMapper.toEntity(entityDTO);
        return tagMapper.toDTO(tagRepository.save(tag));
    }

    @Override
    public TagResponseTo getById(Long id) {
        return tagMapper.toDTO(tagRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tag with ID " + id + " not found")));
    }

    @Override
    public List<TagResponseTo> getAll() {
        return tagRepository.findAll().stream()
                .map(tagMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TagResponseTo update(TagRequestTo entityDTO) {
        Tag tag = tagMapper.toEntity(entityDTO);
        if (!tagRepository.existsById(tag.getId())) {
            throw new EntityNotFoundException("Tag with ID " + tag.getId() + " not found");
        }
        return tagMapper.toDTO(tagRepository.save(tag));
    }

    @Override
    public void deleteById(Long id) {
        if (!tagRepository.existsById(id)) {
            throw new EntityNotFoundException("Tag with ID " + id + " not found");
        }
        tagRepository.deleteById(id);
    }

    public Tag findOrCreateTag(String tagName) {
        Optional<Tag> optionalTag = tagRepository.findByName(tagName);
        if (optionalTag.isPresent()) {
            return optionalTag.get();
        } else {
            TagRequestTo tagRequestTo = new TagRequestTo(null, tagName);
            return tagRepository.save(tagMapper.toEntity(tagRequestTo));
        }
    }
}
