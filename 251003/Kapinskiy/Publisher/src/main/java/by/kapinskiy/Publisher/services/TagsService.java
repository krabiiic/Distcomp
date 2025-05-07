package by.kapinskiy.Publisher.services;

import by.kapinskiy.Publisher.DTOs.Requests.TagRequestDTO;
import by.kapinskiy.Publisher.DTOs.Responses.TagResponseDTO;
import by.kapinskiy.Publisher.models.Issue;
import by.kapinskiy.Publisher.models.Tag;
import by.kapinskiy.Publisher.repositories.IssuesRepository;
import by.kapinskiy.Publisher.repositories.TagsRepository;
import by.kapinskiy.Publisher.utils.exceptions.NotFoundException;
import by.kapinskiy.Publisher.utils.mappers.TagsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagsService {
    private final TagsRepository tagsRepository;
    private final IssuesRepository issuesRepository;
    private final TagsMapper tagsMapper;

    public TagResponseDTO save(Tag tag, long issueId) {
        Issue issue = issuesRepository.findById(issueId).orElseThrow(() -> new NotFoundException("Issue with such id does not exist"));
        issue.getTags().add(tag);
        tag.getIssues().add(issue);
        return tagsMapper.toTagResponse(tagsRepository.save(tag));
    }

    public TagResponseDTO save(TagRequestDTO tagRequestDTO) {
        Tag tag = tagsMapper.toTag(tagRequestDTO);
        return tagsMapper.toTagResponse(tagsRepository.save(tag));
    }

    public List<TagResponseDTO> findAll() {
        return tagsMapper.toTagResponseList(tagsRepository.findAll());
    }

    @CacheEvict(value = "tags", key = "#id")
    public TagResponseDTO findById(long id) {
        return tagsMapper.toTagResponse(tagsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tag with such id does not exist")));
    }

    @CacheEvict(value = "tags", key = "#id")
    public void deleteById(long id) {
        if (!tagsRepository.existsById(id))
            throw new NotFoundException("Tag with such id not found");
        tagsRepository.deleteById(id);
    }

    @CacheEvict(value = "tags", key = "#tagRequestDTO.id")
    public TagResponseDTO update(TagRequestDTO tagRequestDTO) {
        Tag tag = tagsMapper.toTag(tagRequestDTO);
        return tagsMapper.toTagResponse(tagsRepository.save(tag));
    }

    public boolean existsByName(String name) {
        return tagsRepository.existsByName(name);
    }

}
