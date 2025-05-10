package lab.controllers;

import lab.exceptions.DataObjectNotFoundException;
import lab.exceptions.IllegalFieldDataException;
import lab.mappers.PostMapper;
import lab.modelDTOs.PostRequestTo;
import lab.modelDTOs.PostResponseTo;
import lab.models.Post;
import lab.services.IssueService;
import lab.services.PostService;
import lab.services.UserService;
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

    @Autowired
    private IssueService issueService;

    @Autowired
    private UserService userService;


    @GetMapping(value = "")
    ResponseEntity<List<PostResponseTo>> GetPosts() {
        List<PostResponseTo> respList = new ArrayList<>();
        for (Post post : postService.getPosts()) {
            respList.add(PostMapper.INSTANCE.MapPostToResponseDTO(post));
        }
        return new ResponseEntity<>(respList, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    ResponseEntity<PostResponseTo> GetPostById(@PathVariable("id") long id) {
        Post p = postService.findById(id);
        return new ResponseEntity<>(PostMapper.INSTANCE.MapPostToResponseDTO(p), HttpStatus.OK);
    }

    @PostMapping(value = "")
    ResponseEntity<PostResponseTo> CreatePost(@RequestBody PostRequestTo newPost) {
        Post newP = PostMapper.INSTANCE.MapRequestDTOToPost(newPost);
        Post createdPost = postService.createPost(newP);
        return new ResponseEntity<>(PostMapper.INSTANCE.MapPostToResponseDTO(createdPost), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id}")
    ResponseEntity<PostResponseTo> DeletePost(@PathVariable("id") long Id) {
        postService.deletePost(Id);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @PutMapping(value = "")
    ResponseEntity<PostResponseTo> UpdatePost(@RequestBody PostRequestTo updPost) {
        Post newP = PostMapper.INSTANCE.MapRequestDTOToPost(updPost);
        Post updatedPost = postService.updatePost(newP);
        return new ResponseEntity<>(PostMapper.INSTANCE.MapPostToResponseDTO(updatedPost), HttpStatus.OK);
    }
}
