package com.example.JPA2.Impl.Controllers;

import com.example.JPA2.Impl.DTO.PostRequestTo;
import com.example.JPA2.Impl.DTO.PostResponseTo;
import com.example.JPA2.Impl.Services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1.0")
public class PostController {
    @Autowired
    private PostService postService;

    @GetMapping("/posts")
    public Collection<PostResponseTo> getAllPosts() {
        return postService.getAll();
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<PostResponseTo> getPost(@PathVariable long id) {
        try{
            PostResponseTo post = postService.get(id);
            return new ResponseEntity<>(post, HttpStatus.OK);
        }catch (NoSuchElementException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/posts")
    public ResponseEntity<PostResponseTo> createPost(@Valid @RequestBody PostRequestTo PostTo) {
        PostResponseTo addedPost = postService.add(PostTo);
        return new ResponseEntity<>(addedPost, HttpStatus.CREATED);
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<PostResponseTo> deletePost(@PathVariable long id) {
        PostResponseTo deletedPost = postService.delete(id);
        return new ResponseEntity<>(deletedPost, deletedPost == null ? HttpStatus.NOT_FOUND : HttpStatus.NO_CONTENT);
    }

    @PutMapping("/posts")
    public ResponseEntity<PostResponseTo> updatePost(@Valid @RequestBody PostRequestTo PostRequestTo) {
        PostResponseTo PostResponseTo = postService.update(PostRequestTo);
        return new ResponseEntity<>(PostResponseTo, PostResponseTo.getContent() == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
    }
}