package lab1.controllers;

import lab1.Mappers.PostMapper;
import lab1.modelDTOs.PostRequestTo;
import lab1.modelDTOs.PostResponseTo;
import lab1.models.Post;
import lab1.services.PostService;
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

    @GetMapping(value = "")
    ResponseEntity<List<PostResponseTo>> GetPosts(){
        List<PostResponseTo> respList = new ArrayList<>();
        for (Post post : postService.GetPosts()){
            respList.add(PostMapper.INSTANCE.MapPostToResponseDTO(post));
        }
        return new ResponseEntity<>(respList, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    ResponseEntity<PostResponseTo> GetPostById(@PathVariable("id") long id){
        Post p = postService.GetPostById(id);
        if (p != null)
            return new ResponseEntity<>(PostMapper.INSTANCE.MapPostToResponseDTO(p), HttpStatus.OK);
        else
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "")
    ResponseEntity<PostResponseTo> CreatePost(@RequestBody PostRequestTo newPost){
        Post newP = PostMapper.INSTANCE.MapRequestDTOToPost(newPost);
        boolean added = postService.CreatePost(newP);
        if (added)
            return new ResponseEntity<>(PostMapper.INSTANCE.MapPostToResponseDTO(newP), HttpStatus.CREATED);
        else
            return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @DeleteMapping(value = "/{id}")
    ResponseEntity<PostResponseTo> DeletePost(@PathVariable("id") long Id){
        Post del = postService.DeletePost(Id);
        if (del == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }else {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
    }

    @PutMapping(value = "")
    ResponseEntity<PostResponseTo> UpdatePost(@RequestBody PostRequestTo updPost){
        long Id = updPost.getId();
        Post newP = PostMapper.INSTANCE.MapRequestDTOToPost(updPost);
        Post updated = postService.UpdatePost(newP);
        if (updated != null)
            return new ResponseEntity<>(PostMapper.INSTANCE.MapPostToResponseDTO(newP), HttpStatus.OK);
        else
            return new ResponseEntity<>(PostMapper.INSTANCE.MapPostToResponseDTO(newP), HttpStatus.NOT_FOUND);
    }

}
