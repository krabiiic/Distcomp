package com.example.lab1.service;

import com.example.lab1.dto.IssueRequestTo;
import com.example.lab1.dto.IssueResponseTo;
import com.example.lab1.exception.ForbiddenException;
import com.example.lab1.exception.NotFoundException;
import com.example.lab1.model.Issue;
import com.example.lab1.model.Mark;
import com.example.lab1.model.User;
import com.example.lab1.repository.IssueMarkRepository;
import com.example.lab1.repository.IssueRepository;
import com.example.lab1.repository.MarkRepository;
import com.example.lab1.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import com.example.lab1.model.IssueMark;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IssueService {

    private final IssueRepository issueRepository;
    private final UserRepository userRepository;
    private final MarkService markService; // Добавить
    private final IssueMarkRepository issueMarkRepository; // Добавить
    private final MarkRepository markRepository;

    public IssueService(IssueRepository issueRepository,
                        UserRepository userRepository,
                        MarkService markService,
                        IssueMarkRepository issueMarkRepository, MarkRepository markRepository) {
        this.issueRepository = issueRepository;
        this.userRepository = userRepository;
        this.markService = markService;
        this.issueMarkRepository = issueMarkRepository;
        this.markRepository = markRepository;
    }
    
    public IssueResponseTo createIssue(IssueRequestTo request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found", 40402));
        if (issueRepository.existsByTitle(request.getTitle())) {
            throw new ForbiddenException("Title already exists", 40303);
        }
        Issue issue = new Issue();
        issue.setUser(user);
        issue.setTitle(request.getTitle());
        issue.setContent(request.getContent());
        issue.setCreated(LocalDateTime.now());
        issue.setModified(LocalDateTime.now());
        Issue saved = issueRepository.save(issue);
        if (request.getMarks() != null) {
            for (String markName : request.getMarks()) {
                Mark mark = markService.findOrCreateMark(markName);
                IssueMark issueMark = new IssueMark();
                issueMark.setIssue(saved);
                issueMark.setMark(mark);
                issueMarkRepository.save(issueMark);
            }
        }
        return toDto(saved);
    }
    
    public List<IssueResponseTo> getAllIssues() {
        return issueRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    
    public IssueResponseTo getIssueById(Long id) {
        Issue issue = issueRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Issue not found", 40402));
        return toDto(issue);
    }
    
    public IssueResponseTo updateIssue(Long id, IssueRequestTo request) {
        Issue issue = issueRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Issue not found", 40402));
        if(!issue.getUser().getId().equals(request.getUserId())) {
            User user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new ForbiddenException("User not found for issue update", 40300));
            issue.setUser(user);
        }
        issue.setTitle(request.getTitle());
        issue.setContent(request.getContent());
        issue.setModified(LocalDateTime.now());
        Issue updated = issueRepository.save(issue);
        return toDto(updated);
    }
    
    public void deleteIssue(Long id) {
        if(!issueRepository.existsById(id)) {
            throw new NotFoundException("Issue not found", 40402);
        }
        List<IssueMark> list = issueMarkRepository.findByIssue(issueRepository.findById(id));
        for (IssueMark issueMark : list) {
            Mark mark = issueMark.getMark();
            issueMarkRepository.delete(issueMark);
            markRepository.delete(mark);
        }
        issueRepository.deleteById(id);
    }
    
    private IssueResponseTo toDto(Issue issue) {
        IssueResponseTo dto = new IssueResponseTo();
        dto.setId(issue.getId());
        dto.setUserId(issue.getUser().getId());
        dto.setTitle(issue.getTitle());
        dto.setContent(issue.getContent());
        dto.setCreated(issue.getCreated());
        dto.setModified(issue.getModified());

        List<String> marks = issueMarkRepository.findByIssue(issue).stream()
                .map(im -> im.getMark().getName())
                .collect(Collectors.toList());
        dto.setMarks(marks);

        return dto;
    }
}
