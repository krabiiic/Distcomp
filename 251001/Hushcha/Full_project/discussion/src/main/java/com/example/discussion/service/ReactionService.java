package com.example.discussion.service;

import com.example.discussion.dto.ReactionRequestTo;
import com.example.discussion.dto.ReactionResponseTo;
import com.example.discussion.entity.Reaction;
import com.example.discussion.entity.ReactionKey;
import com.example.discussion.mapper.ReactionMapper;
import com.example.discussion.repository.ReactionRepository;
import com.example.discussion.service.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReactionService{

    private final ReactionRepository repository;
    private final ReactionMapper mapper = ReactionMapper.INSTANCE;

    public ReactionResponseTo create(ReactionRequestTo request) {
        Reaction reaction = mapper.toEntity(request);
        Reaction saved = repository.save(reaction);
        return mapper.toDto(saved);
    }

    public List<ReactionResponseTo> getAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public ReactionResponseTo getById(Long id) {
        Reaction reaction = repository.findFirstById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reaction not found with id " + id));
        return mapper.toDto(reaction);
    }

    public void deleteById(Long id) {
        Reaction reaction = repository.findFirstById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reaction not found with id " + id));
        repository.delete(reaction);
    }

    public ReactionResponseTo updateReaction(ReactionRequestTo reactionRequestTo) {
        Reaction reaction = repository.findFirstById(reactionRequestTo.id())
                .orElseThrow(() -> new EntityNotFoundException("Reaction not found with id " + reactionRequestTo.id()));

        reaction.setContent(reactionRequestTo.content());
        reaction.getId().setTopicId(reactionRequestTo.newsId());

        repository.save(reaction);

        return mapper.toDto(reaction);
    }
}