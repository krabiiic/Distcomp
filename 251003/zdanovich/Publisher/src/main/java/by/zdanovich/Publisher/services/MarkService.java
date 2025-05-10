package by.zdanovich.Publisher.services;

import by.zdanovich.Publisher.DTOs.Requests.MarkRequestDTO;
import by.zdanovich.Publisher.DTOs.Responses.MarkResponseDTO;
import by.zdanovich.Publisher.models.Issue;
import by.zdanovich.Publisher.models.Mark;
import by.zdanovich.Publisher.repositories.IssueRepository;
import by.zdanovich.Publisher.repositories.MarkRepository;
import by.zdanovich.Publisher.utils.exceptions.NotFoundException;
import by.zdanovich.Publisher.utils.mappers.MarkMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MarkService {
    private final MarkRepository markRepository;
    private final IssueRepository issueRepository;
    private final MarkMapper markMapper;

    public MarkResponseDTO save(Mark mark, long issueId) {
        Issue issue = issueRepository.findById(issueId).orElseThrow(() -> new NotFoundException("Issue with such id does not exist"));
        issue.getMarks().add(mark);
        mark.getIssues().add(issue);
        return markMapper.toMarkResponse(markRepository.save(mark));
    }

    public MarkResponseDTO save(MarkRequestDTO markRequestDTO) {
        Mark mark = markMapper.toMark(markRequestDTO);
        return markMapper.toMarkResponse(markRepository.save(mark));
    }

    public List<MarkResponseDTO> findAll() {
        return markMapper.toMarkResponseList(markRepository.findAll());
    }

    @CacheEvict(value = "marks", key = "#id")
    public MarkResponseDTO findById(long id) {
        return markMapper.toMarkResponse(markRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Mark with such id does not exist")));
    }

    @CacheEvict(value = "marks", key = "#id")
    public void deleteById(long id) {
        if (!markRepository.existsById(id))
            throw new NotFoundException("Mark with such id not found");
        markRepository.deleteById(id);
    }

    @CacheEvict(value = "marks", key = "#markRequestDTO.id")
    public MarkResponseDTO update(MarkRequestDTO markRequestDTO) {
        Mark mark = markMapper.toMark(markRequestDTO);
        return markMapper.toMarkResponse(markRepository.save(mark));
    }

    public boolean existsByName(String name) {
        return markRepository.existsByName(name);
    }

}
