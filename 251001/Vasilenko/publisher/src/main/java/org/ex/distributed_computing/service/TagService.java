package org.ex.distributed_computing.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.ex.distributed_computing.dto.request.TagRequestDTO;
import org.ex.distributed_computing.dto.response.TagResponseDTO;
import org.ex.distributed_computing.exception.NotFoundException;
import org.ex.distributed_computing.mapper.TagMapper;
import org.ex.distributed_computing.model.Tag;
import org.ex.distributed_computing.repository.TagRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TagService {

  private final TagRepository tagRepository;
  private final TagMapper tagMapper;

  public List<TagResponseDTO> getAllTags() {
    return tagMapper.toDtoList(tagRepository.findAll());
  }

  public TagResponseDTO getTagById(Long id) {
    Tag tag = tagRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Tag not found"));
    return tagMapper.toDto(tag);
  }

  public TagResponseDTO createTag(TagRequestDTO requestDTO) {
    Tag tag = tagMapper.toEntity(requestDTO);
    tagRepository.save(tag);
    return tagMapper.toDto(tag);
  }

  public TagResponseDTO updateTag(TagRequestDTO requestDTO) {
    Tag tag = tagRepository.findById(requestDTO.id())
        .orElseThrow(() -> new NotFoundException("Tag not found"));

    tag.setName(requestDTO.name());
    tagRepository.save(tag);
    return tagMapper.toDto(tag);
  }

  public void deleteTag(Long id) {
    Tag tag = tagRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Tag not found"));
    tagRepository.delete(tag);
  }
}

