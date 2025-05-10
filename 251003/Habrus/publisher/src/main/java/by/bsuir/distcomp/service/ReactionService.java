package by.bsuir.distcomp.service;

import by.bsuir.distcomp.dto.mapper.ReactionMapper;
import by.bsuir.distcomp.dto.request.ReactionRequestTo;
import by.bsuir.distcomp.dto.response.ReactionResponseTo;
import by.bsuir.distcomp.repository.ReactionRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ReactionService {

    private final ReactionRepository reactionRepository;
    private final ReactionMapper reactionMapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public ReactionService(
            ReactionRepository reactionRepository,
            ReactionMapper reactionMapper,
            KafkaTemplate<String, Object> kafkaTemplate
    ) {
        this.reactionRepository = reactionRepository;
        this.reactionMapper = reactionMapper;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Cacheable(value = "reactions", key = "'all'")
    public List<ReactionResponseTo> getAllReactions() {
        return reactionRepository.findAll().stream().map(reactionMapper::toDto).toList();
    }

    @Cacheable(value = "reactions", key = "#id")
    public ReactionResponseTo getReactionById(Long id) {
        return reactionRepository.findById(id)
                .map(reactionMapper::toDto)
                .orElseThrow(() -> new NoSuchElementException("Reaction with id: " + id + " not found"));
    }

    @Transactional
    @CacheEvict(value = "reactions", key = "'all'")
    public ReactionResponseTo createReaction(ReactionRequestTo reactionRequestTo) {
        ReactionResponseTo reactionResponseTo = reactionMapper.toDto(reactionRepository.save(reactionMapper.toEntity(reactionRequestTo)));

        reactionRequestTo.setId(reactionResponseTo.getId());
        sendToCreate(reactionRequestTo);

        return reactionResponseTo;
    }

    @Transactional
    @Caching(
            put = @CachePut(value = "reactions", key = "#reactionRequestTo.id"),
            evict = @CacheEvict(value = "reactions", key = "'all'")
    )
    public ReactionResponseTo updateReaction(ReactionRequestTo reactionRequestTo) {
        Long id = reactionRequestTo.getId();
        reactionRepository
                .findById(id)
                .orElseThrow(() -> new NoSuchElementException("Reaction with id: " + id + " not found"));

        ReactionResponseTo reactionResponseTo = reactionMapper.toDto(reactionRepository.save(reactionMapper.toEntity(reactionRequestTo)));

        sendToUpdate(reactionRequestTo);

        return reactionResponseTo;
    }

    @Transactional

    @Caching (
            evict = {
                    @CacheEvict(value = "reactions", key = "#id"),
                    @CacheEvict(value = "reactions", key = "'all'")
            }
    )
    public void deleteReaction(Long id) {
        reactionRepository
                .findById(id)
                .orElseThrow(() -> new NoSuchElementException("Reaction with id: " + id + " not found"));

        sendToDelete(id);

        reactionRepository.deleteById(id);
    }

    public void sendToCreate(ReactionRequestTo req) {
        kafkaTemplate.send("create", req);
    }

    public void sendToUpdate(ReactionRequestTo req) {
        kafkaTemplate.send("update", req);
    }

    public void sendToDelete(Long id) {
        kafkaTemplate.send("delete", id);
    }

}
