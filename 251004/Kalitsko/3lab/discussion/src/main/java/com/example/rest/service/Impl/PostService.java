package com.example.rest.service.Impl;

import com.example.rest.dto.PostRequestTo;
import com.example.rest.dto.PostResponseTo;
import com.example.rest.dto.PostUpdate;
import com.example.rest.entity.Post;
import com.example.rest.exceptionHandler.CreatorNotFoundException;
import com.example.rest.mapper.PostMapper;
import com.example.rest.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;

    private final PostMapper postMapper;

    public PostService(PostRepository postRepository, PostMapper postMapper) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
    }

    // Создание нового поста
    public PostResponseTo createPost(PostRequestTo requestTo) {
        Post post = postMapper.toEntity(requestTo);
        post.setCountry("Default");
        post.setId((long) (Math.random() * 10000000));
        Post savedPost = postRepository.save(post);
        return postMapper.toResponse(savedPost);
    }

    // Получение всех постов
    public List<PostResponseTo> getAllPosts() {
        return postRepository.findAll().stream()
                .map(postMapper::toResponse)
                .collect(Collectors.toList());
    }

    // Получение поста по ключу
/*    public PostResponseTo getPostById(Long id) {
        Optional<Post> post = postRepository.findById(id);
        return post.map(postMapper::toResponse).orElseThrow(() -> new RuntimeException("Post not found"));
    }*/

    public PostResponseTo getPostById(Long id) {
        List<Post> allPosts = postRepository.findAll();
        return allPosts.stream()
                .filter(post -> post.getId().equals(id))
                .findFirst()
                .map(postMapper::toResponse)
                .orElseThrow(() -> new CreatorNotFoundException(id));
    }

    // Обновление поста
    public PostResponseTo updatePost(PostUpdate postUpdate) {
/*        Post post = postRepository.findById(postUpdate.getId())
                .orElseThrow(() -> new RuntimeException("Post not found"));*/

        Post currPost = postRepository.findAll().stream()
                .filter(post -> post.getId().equals(postUpdate.getId()))
                .findFirst()
                .orElseThrow(() -> new CreatorNotFoundException(postUpdate.getId()));

        currPost.setContent(postUpdate.getContent());
        Post updatedPost = postRepository.save(currPost);
        return postMapper.toResponse(updatedPost);
    }

    // Удаление поста
    public void deletePost(Long id) {
        Post currPost = postRepository.findAll().stream()
                .filter(post -> post.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new CreatorNotFoundException(id));
        if (currPost != null) {
            postRepository.delete(currPost);
        } else {
            throw new CreatorNotFoundException(id);
        }
    }
}