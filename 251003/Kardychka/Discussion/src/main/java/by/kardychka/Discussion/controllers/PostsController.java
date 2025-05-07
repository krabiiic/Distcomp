package by.kardychka.Discussion.controllers;


import by.kardychka.Discussion.DTOs.Requests.PostRequestDTO;
import by.kardychka.Discussion.DTOs.Responses.PostResponseDTO;
import by.kardychka.Discussion.services.PostsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostsController {
    private final PostsService postsService;

    @Autowired
    public PostsController(PostsService postsService) {
        this.postsService = postsService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PostResponseDTO> getAllPosts() {
        return postsService.findAll();
    }

    @GetMapping("/{id}")
    public PostResponseDTO getPostById(@PathVariable Long id) {
        try {
            return postsService.findById(id);
        } catch (RuntimeException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public PostResponseDTO updatePost(@RequestBody @Valid PostRequestDTO noteRequestDTO) {
        return postsService.update(noteRequestDTO);
    }

}
