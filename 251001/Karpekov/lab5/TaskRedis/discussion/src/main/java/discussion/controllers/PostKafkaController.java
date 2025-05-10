package discussion.controllers;

import discussion.mappers.PostMapper;
import discussion.modelDTOs.PostRequestTo;
import discussion.modelDTOs.PostResponseTo;
import discussion.models.Post;
import discussion.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Controller
public class PostKafkaController {
    @Autowired
    private PostService postService;

    public List<PostResponseTo> getAll(){
        List<PostResponseTo> respList = new ArrayList<>();
        for (Post post : postService.getPosts()) {
            respList.add(PostMapper.INSTANCE.MapPostToResponseDTO(post));
        }
        return respList;
    }
    public PostResponseTo getById(Long id){
        return PostMapper.INSTANCE.MapPostToResponseDTO(postService.findById(id));
    }
    public PostResponseTo create(PostRequestTo request){
        Post newP = PostMapper.INSTANCE.MapRequestDTOToPost(request);
        Post createdPost = postService.createPost(newP);
        return PostMapper.INSTANCE.MapPostToResponseDTO(createdPost);
    }
    public void delete(Long id){
        postService.deletePost(id);
    }
    public PostResponseTo update(PostRequestTo request){
        Post newP = PostMapper.INSTANCE.MapRequestDTOToPost(request);
        Post updatedPost = postService.updatePost(newP);
        return PostMapper.INSTANCE.MapPostToResponseDTO(updatedPost);
    }
}
