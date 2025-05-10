package by.kardychka.Publisher.controllers;

import by.kardychka.Publisher.DTOs.Requests.PostRequestDTO;
import by.kardychka.Publisher.DTOs.Responses.PostResponseDTO;
import by.kardychka.Publisher.services.PostsService;
import by.kardychka.Publisher.utils.PostValidator;
import by.kardychka.Publisher.utils.exceptions.NotFoundException;
import by.kardychka.Publisher.utils.exceptions.ValidationException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostsController {

    private final PostsService postsService;
    private final PostValidator postsValidator;
    @Autowired
    public PostsController(PostsService postsService, PostValidator postsValidator) {
        this.postsService = postsService;
        this.postsValidator = postsValidator;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostResponseDTO createPost(@RequestBody @Valid PostRequestDTO postRequestDTO,
                                            BindingResult bindingResult) {
        validateRequest(postRequestDTO, bindingResult);
        return postsService.createPost(postRequestDTO);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PostResponseDTO getPostById(@PathVariable Long id) {
        return postsService.getPostById(id);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<PostResponseDTO> getAllPosts() {
        return postsService.getAllPosts();
    }

    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public PostResponseDTO updatePost(
            @RequestBody @Valid PostRequestDTO postRequestDTO) {
        return postsService.processPostRequest("PUT", postRequestDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(@PathVariable Long id) {
        PostRequestDTO request = new PostRequestDTO();
        request.setId(id);
        postsService.processPostRequest("DELETE", request);
    }

    private void validateRequest(PostRequestDTO request, BindingResult bindingResult) {
        if (!bindingResult.hasFieldErrors()) {
            postsValidator.validate(request, bindingResult);
        }
        if (bindingResult.hasFieldErrors()) {
            throw new ValidationException(bindingResult);
        }
    }
}
