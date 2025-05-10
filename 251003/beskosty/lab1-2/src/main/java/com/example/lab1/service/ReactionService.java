package com.example.lab1.service;

import com.example.lab1.dto.ReactionRequestTo;
import com.example.lab1.dto.ReactionResponseTo;
import com.example.lab1.exception.NotFoundException;
import com.example.lab1.model.Issue;
import com.example.lab1.model.Reaction;
import com.example.lab1.repository.IssueRepository;
import com.example.lab1.repository.ReactionRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReactionService {

    private final ReactionRepository reactionRepository;
    private final IssueRepository issueRepository;
    
    public ReactionService(ReactionRepository reactionRepository, IssueRepository issueRepository) {
        this.reactionRepository = reactionRepository;
        this.issueRepository = issueRepository;
    }
    
    public ReactionResponseTo createReaction(ReactionRequestTo request) {
        Issue issue = issueRepository.findById(request.getIssueId())
                .orElseThrow(() -> new NotFoundException("Issue not found for reaction", 40403));
        Reaction reaction = new Reaction();
        reaction.setIssue(issue);
        reaction.setContent(request.getContent());
        Reaction saved = reactionRepository.save(reaction);
        return toDto(saved);
    }
    
    public List<ReactionResponseTo> getAllReactions() {
        return reactionRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    
    public ReactionResponseTo getReactionById(Long id) {
        Reaction reaction = reactionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Reaction not found", 40403));
        return toDto(reaction);
    }
    
    public ReactionResponseTo updateReaction(Long id, ReactionRequestTo request) {
        Reaction reaction = reactionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Reaction not found", 40403));
        reaction.setContent(request.getContent());
        Reaction updated = reactionRepository.save(reaction);
        return toDto(updated);
    }
    
    public void deleteReaction(Long id) {
        if(!reactionRepository.existsById(id)) {
            throw new NotFoundException("Reaction not found", 40403);
        }
        reactionRepository.deleteById(id);
    }
    
    private ReactionResponseTo toDto(Reaction reaction) {
        ReactionResponseTo dto = new ReactionResponseTo();
        dto.setId(reaction.getId());
        dto.setIssueId(reaction.getIssue().getId());
        dto.setContent(reaction.getContent());
        return dto;
    }
}
