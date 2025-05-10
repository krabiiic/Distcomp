package discussion.controllers;

import discussion.mappers.PostMapper;
import discussion.modelDTOs.PostRequestTo;
import discussion.modelDTOs.PostResponseTo;
import discussion.models.Post;
import discussion.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "api/v1.0/posts")
public class PostController {

    @Autowired
    private PostService postService;

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
