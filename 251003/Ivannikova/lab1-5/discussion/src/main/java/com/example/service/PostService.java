package com.example.service;

import com.example.dto.PostRequestTo;
import com.example.dto.PostResponseTo;
import com.example.exception.NotFoundException;
import com.example.model.Post;
import com.example.model.Post.PostKey;
import com.example.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class PostService {

    private final PostRepository reactionRepository;

    public PostService(PostRepository reactionRepository) {
        this.reactionRepository = reactionRepository;
    }

    public PostResponseTo createReaction(PostRequestTo request) {
        PostKey key = new PostKey(request.getCountry(), request.getIssueId(), request.getId());
        Post reaction = new Post(key, request.getContent());
        Post saved = reactionRepository.save(reaction);
        return toDto(saved);
    }

    public PostResponseTo updateReaction(PostRequestTo request) {
        PostKey key = new PostKey(request.getCountry(), request.getIssueId(), request.getId());
        Post reaction = reactionRepository.findById(key)
                .orElseThrow(() -> new NotFoundException("Reaction not found", 40400));
        reaction.setContent(request.getContent());
        Post updated = reactionRepository.save(reaction);
        return toDto(updated);
    }

    public PostResponseTo getReaction(Long id) {
        Post reaction = reactionRepository.findAll().stream()
                .filter(r -> r.getKey().getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Reaction not found", 40401));
        return toDto(reaction);
    }

    public List<PostResponseTo> getAllReactions() {
        return reactionRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public void deleteReaction(Long id) {
        Post reaction = reactionRepository.findAll().stream()
                .filter(r -> r.getKey().getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Reaction not found", 40402));
        reactionRepository.delete(reaction);
    }

    private PostResponseTo toDto(Post reaction) {
        PostResponseTo dto = new PostResponseTo();
        dto.setCountry(reaction.getKey().getCountry());
        dto.setIssueId(reaction.getKey().getIssueId());
        dto.setId(reaction.getKey().getId());
        dto.setContent(reaction.getContent());
        return dto;
    }

    public Post save(Post reaction) {
        return reactionRepository.save(reaction);
    }

    public Optional<Post> findById(Long id) {
        return StreamSupport.stream(reactionRepository.findAll().spliterator(), false)
                .filter(r -> r.getKey().getId().equals(id))
                .findFirst();
    }

    public Optional<Post> findById(Post.PostKey key) {
        return reactionRepository.findById(key);
    }

    public List<Post> findAll() {
        return StreamSupport.stream(reactionRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public void deleteById(Post.PostKey key) {
        reactionRepository.deleteById(key);
    }
}
