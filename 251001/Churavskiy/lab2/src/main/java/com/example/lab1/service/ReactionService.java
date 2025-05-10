package com.example.lab1.service;

import com.example.lab1.dto.ReactionRequestTo;
import com.example.lab1.dto.ReactionResponseTo;
import com.example.lab1.entity.Reaction;
import com.example.lab1.entity.Topic;
import com.example.lab1.mapper.ReactionMapper;
import com.example.lab1.repository.ReactionRepository;
import com.example.lab1.repository.TopicRepository;
import com.example.lab1.service.exception.TopicNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReactionService implements EntityService<ReactionRequestTo, ReactionResponseTo> {

    private final ReactionRepository reactionRepository;
    private final TopicRepository topicRepository;
    private final ReactionMapper reactionMapper = ReactionMapper.INSTANCE;

    @Override
    public ReactionResponseTo create(ReactionRequestTo entityDTO) {
        Optional<Topic> topicOptional = topicRepository.findById(entityDTO.topicId());

        if (topicOptional.isEmpty()) {
            throw new TopicNotFoundException("Optional with id " + entityDTO.topicId() + " not found");
        }

        Reaction reaction = reactionMapper.toEntity(entityDTO);
        return reactionMapper.toDTO(reactionRepository.save(reaction));
    }

    @Override
    public ReactionResponseTo getById(Long id) {
        return reactionMapper.toDTO(reactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reaction with ID " + id + " not found")));
    }

    @Override
    public List<ReactionResponseTo> getAll() {
        return reactionRepository.findAll().stream()
                .map(reactionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ReactionResponseTo update(ReactionRequestTo entityDTO) {
        Reaction reaction = reactionMapper.toEntity(entityDTO);
        if (!reactionRepository.existsById(reaction.getId())) {
            throw new EntityNotFoundException("Reaction with ID " + reaction.getId() + " not found");
        }
        return reactionMapper.toDTO(reactionRepository.save(reaction));
    }

    @Override
    public void deleteById(Long id) {
        if (!reactionRepository.existsById(id)) {
            throw new EntityNotFoundException("Reaction with ID " + id + " not found");
        }
        reactionRepository.deleteById(id);
    }
}
