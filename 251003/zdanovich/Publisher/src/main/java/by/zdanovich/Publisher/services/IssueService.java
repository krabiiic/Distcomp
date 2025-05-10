package by.zdanovich.Publisher.services;

import by.zdanovich.Publisher.DTOs.Requests.IssueRequestDTO;
import by.zdanovich.Publisher.DTOs.Responses.IssueResponseDTO;
import by.zdanovich.Publisher.models.Issue;
import by.zdanovich.Publisher.models.Mark;
import by.zdanovich.Publisher.models.Writer;
import by.zdanovich.Publisher.repositories.IssueRepository;
import by.zdanovich.Publisher.repositories.WriterRepository;
import by.zdanovich.Publisher.utils.exceptions.NotFoundException;
import by.zdanovich.Publisher.utils.mappers.IssueMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IssueService {

    private final IssueRepository issueRepository;
    private final WriterRepository writerRepository;
    private final IssueMapper issueMapper;

    private void setWriter(Issue issue, long writerId) {
        Writer writer = writerRepository.findById(writerId)
                .orElseThrow(() -> new NotFoundException("Writer with such id does not exist"));
        issue.setWriter(writer);
    }

    @Transactional
    public IssueResponseDTO save(IssueRequestDTO issueRequestDTO) {
        Issue issue = issueMapper.toIssue(issueRequestDTO);
        setWriter(issue, issueRequestDTO.getWriterId());
        if (!issueRequestDTO.getMarks().isEmpty()) {
            issue.setMarks(issueRequestDTO.getMarks().stream().map(Mark::new).toList());
        }
        issue.setCreated(new Date());
        issue.setModified(new Date());
        return issueMapper.toIssueResponse(issueRepository.save(issue));
    }

    @Transactional(readOnly = true)
    public List<IssueResponseDTO> findAll() {
        return issueMapper.toIssueResponseList(issueRepository.findAll());
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "issues", key = "#id")
    public IssueResponseDTO findById(long id) {
        return issueMapper.toIssueResponse(
                issueRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException("Issue with such id does not exist")));
    }

    @Transactional
    @CacheEvict(value = "issues", key = "#id")
    public void deleteById(long id) {
        if (!issueRepository.existsById(id)) {
            throw new NotFoundException("Issue not found");
        }
        issueRepository.deleteById(id);
    }

    @Transactional
    @CacheEvict(value = "issues", key = "#issueRequestDTO.id")
    public IssueResponseDTO update(IssueRequestDTO issueRequestDTO) {
        Issue issue = issueMapper.toIssue(issueRequestDTO);
        Issue oldIssue = issueRepository.findById(issue.getId())
                .orElseThrow(() -> new NotFoundException("Old issue not found"));
        Long writerId = issueRequestDTO.getWriterId();

        if (writerId != null) {
            setWriter(issue, writerId);
        }
        issue.setCreated(oldIssue.getCreated());
        issue.setModified(new Date());
        return issueMapper.toIssueResponse(issueRepository.save(issue));
    }

    public boolean existsByTitle(String title) {
        return issueRepository.existsByTitle(title);
    }

    public boolean existsById(Long id) {
        return issueRepository.existsById(id);
    }
}
