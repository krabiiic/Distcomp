package com.example.publisher.service;

import com.example.publisher.api.dto.request.IssueRequestTo;
import com.example.publisher.api.dto.responce.IssueResponseTo;
import com.example.publisher.mapper.IssueMapper;
import com.example.publisher.model.Issue;
import com.example.publisher.model.User;
import com.example.publisher.repository.IssueRepository;
import com.example.publisher.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;

@Service
public class IssueService {
    private final IssueRepository issueRepository;
    private final UserRepository userRepository;
    private final IssueMapper issueMapper;


    @Autowired
    public IssueService(IssueRepository issueRepository, IssueMapper issueMapper, UserRepository userRepository) {
        this.issueRepository = issueRepository;
        this.issueMapper = issueMapper;
        this.userRepository = userRepository;
    }

    public IssueResponseTo create(IssueRequestTo request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (issueRepository.existsByTitle(request.getTitle())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);

        }
        Issue issue = issueMapper.fromRequestToEntity(request);
        issue.setUser(user);
        issue.setCreated(Instant.now());
        issue.setModified(Instant.now());
        return issueMapper.fromEntityToResponse(issueRepository.save(issue));
    }

    public List<IssueResponseTo> getAll() {
        return issueRepository.findAll().stream()
                .map(issueMapper::fromEntityToResponse)
                .toList();
    }

    public IssueResponseTo getById(Long id) {
        return issueRepository.findById(id)
                .map(issueMapper::fromEntityToResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public IssueResponseTo update(IssueRequestTo request) {
        Issue entity = issueRepository.findById(request.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        issueMapper.updateEntity(entity, request);
        entity.setModified(Instant.now());
        return issueMapper.fromEntityToResponse(issueRepository.save(entity));

    }

    public void delete(Long id) {
        if (issueRepository.existsById(id)) {
            issueRepository.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
