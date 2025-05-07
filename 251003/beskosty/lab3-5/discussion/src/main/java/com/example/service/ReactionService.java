package com.example.service;

import com.example.dto.ReactionRequestTo;
import com.example.dto.ReactionResponseTo;
import com.example.exception.NotFoundException;
import com.example.model.Reaction;
import com.example.model.Reaction.ReactionKey;
import com.example.repository.ReactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ReactionService {

    private final ReactionRepository reactionRepository;

    public ReactionService(ReactionRepository reactionRepository) {
        this.reactionRepository = reactionRepository;
    }

    public ReactionResponseTo createReaction(ReactionRequestTo request) {
        ReactionKey key = new ReactionKey(request.getCountry(), request.getIssueId(), request.getId());
        Reaction reaction = new Reaction(key, request.getContent());
        Reaction saved = reactionRepository.save(reaction);
        return toDto(saved);
    }

    public ReactionResponseTo updateReaction(ReactionRequestTo request) {
        ReactionKey key = new ReactionKey(request.getCountry(), request.getIssueId(), request.getId());
        Reaction reaction = reactionRepository.findById(key)
                .orElseThrow(() -> new NotFoundException("Reaction not found", 40400));
        reaction.setContent(request.getContent());
        Reaction updated = reactionRepository.save(reaction);
        return toDto(updated);
    }

    public ReactionResponseTo getReaction(Long id) {
        Reaction reaction = reactionRepository.findAll().stream()
                .filter(r -> r.getKey().getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Reaction not found", 40401));
        return toDto(reaction);
    }

    public List<ReactionResponseTo> getAllReactions() {
        return reactionRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public void deleteReaction(Long id) {
        Reaction reaction = reactionRepository.findAll().stream()
                .filter(r -> r.getKey().getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Reaction not found", 40402));
        reactionRepository.delete(reaction);
    }

    private ReactionResponseTo toDto(Reaction reaction) {
        ReactionResponseTo dto = new ReactionResponseTo();
        dto.setCountry(reaction.getKey().getCountry());
        dto.setIssueId(reaction.getKey().getIssueId());
        dto.setId(reaction.getKey().getId());
        dto.setContent(reaction.getContent());
        return dto;
    }

    public Reaction save(Reaction reaction) {
        return reactionRepository.save(reaction);
    }

    public Optional<Reaction> findById(Long id) {
        return StreamSupport.stream(reactionRepository.findAll().spliterator(), false)
                .filter(r -> r.getKey().getId().equals(id))
                .findFirst();
    }

    public Optional<Reaction> findById(Reaction.ReactionKey key) {
        return reactionRepository.findById(key);
    }

    public List<Reaction> findAll() {
        return StreamSupport.stream(reactionRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public void deleteById(Reaction.ReactionKey key) {
        reactionRepository.deleteById(key);
    }
}
