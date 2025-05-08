package com.example.Rest1.Impl.Controllers;

import com.example.Rest1.Impl.DTO.PostResponseTo;
import com.example.Rest1.Impl.DTO.PostRequestTo;
import com.example.Rest1.Impl.Services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1.0")
public class PostController {
    @Autowired
    private PostService postService;

    @GetMapping("/posts")
    public ResponseEntity<List<PostResponseTo>> getAllPost() {
        List<PostResponseTo> postResponseToList = postService.getAll();
        return new ResponseEntity<>(postResponseToList, HttpStatus.OK);
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<PostResponseTo> getPost(@PathVariable long id) {
        PostResponseTo PostResponseTo = postService.get(id);
        return new ResponseEntity<>(PostResponseTo, PostResponseTo == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
    }

    @PostMapping("/posts")
    public ResponseEntity<PostResponseTo> createPost(@RequestBody PostRequestTo PostTo) {
        PostResponseTo addedPost = postService.add(PostTo);
        return new ResponseEntity<>(addedPost, HttpStatus.CREATED);
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<PostResponseTo> deletePost(@PathVariable long id) {
        PostResponseTo deletedPost = postService.delete(id);
        return new ResponseEntity<>(deletedPost, deletedPost == null ? HttpStatus.NOT_FOUND : HttpStatus.NO_CONTENT);
    }

    @PutMapping("/posts")
    public ResponseEntity<PostResponseTo> updatePost(@RequestBody PostRequestTo PostRequestTo) {
        PostResponseTo PostResponseTo = postService.update(PostRequestTo);
        return new ResponseEntity<>(PostResponseTo, PostResponseTo.getContent() == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
    }
}