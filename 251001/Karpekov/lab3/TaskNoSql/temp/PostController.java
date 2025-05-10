package publisher.controllers;

import publisher.modelDTOs.PostRequestTo;
import publisher.modelDTOs.PostResponseTo;
import publisher.services.IssueService;
import publisher.services.PostService;
import publisher.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "api/v1.0/posts", consumes = {"application/JSON"}, produces = {"application/JSON"})
public class PostController {

    @Autowired
    private PostService postService;


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public PostResponseTo createPost(@RequestBody PostRequestTo postRequestTo) {
        return postService.createPost(postRequestTo).block();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public PostResponseTo getPost(@PathVariable Long id) {
        return postService.findPostById(id).block();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable Long id) {
        postService.deletePost(id).block();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<PostResponseTo> getAllPosts() {
        return postService.getPosts()
                .collectList()
                .block();
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping
    public PostResponseTo updatePost(@RequestBody PostRequestTo postRequestTo) {
        return postService.updatePost(postRequestTo);
    }
}
