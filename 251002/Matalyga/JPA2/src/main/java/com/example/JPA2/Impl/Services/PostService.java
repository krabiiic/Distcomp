package com.example.JPA2.Impl.Services;

import com.example.JPA2.Exceptions.NotFoundException;
import com.example.JPA2.Exceptions.Violation;
import com.example.JPA2.Impl.CRUD.PostCRUD;
import com.example.JPA2.Impl.DTO.PostRequestTo;
import com.example.JPA2.Impl.DTO.PostResponseTo;
import com.example.JPA2.Impl.Entity.News;
import com.example.JPA2.Impl.Entity.Post;
import com.example.JPA2.Impl.Repository.NewsRepository;
import com.example.JPA2.Impl.Repository.PostRepository;
import com.example.JPA2.Mappers.PostListMapper;
import com.example.JPA2.Mappers.PostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PostService implements Service<PostResponseTo, PostRequestTo> {
    @Autowired
    private PostCRUD postCRUD;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private PostListMapper postListMapper;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private NewsRepository newsRepository;

    public PostService() {}

    public List<PostResponseTo> getAll() {
        return postRepository.getAll()
                .map(postMapper::PostToPostResponseTo)
                .toList();
    }

    public PostResponseTo update(PostRequestTo updatingPost) {
        try{
            Post post = postMapper.PostRequestToToPost(updatingPost);
            News news = newsRepository.findById(updatingPost.getNewsId()).orElseThrow(() -> new NotFoundException("No News Found", 404));
            post.setNews(news);
            return postMapper.PostToPostResponseTo(postRepository.save(post));
        }catch (DataIntegrityViolationException e){
            throw new Violation("Already taken");
        }
    }

    public PostResponseTo get(long id) {
        return postRepository.get(id).map(postMapper::PostToPostResponseTo).orElse(null);
    }


    public PostResponseTo delete(long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new NotFoundException("No Post Found", 404));
        postRepository.delete(post);
        return postMapper.PostToPostResponseTo(post);
    }

    public PostResponseTo add(PostRequestTo postRequestTo) {
        try{
            News news = newsRepository.findById(postRequestTo.getNewsId()).orElseThrow(() -> new Violation("News not found"));
            Post post = postMapper.PostRequestToToPost(postRequestTo);
            post.setNews(news);
            return postRepository.add(post).map(postMapper::PostToPostResponseTo).orElseThrow();
        } catch (DataIntegrityViolationException e){
            throw new Violation("Already taken");
        }
    }
}
