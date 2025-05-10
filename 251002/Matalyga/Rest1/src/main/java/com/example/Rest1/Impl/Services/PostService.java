package com.example.Rest1.Impl.Services;

import com.example.Rest1.Impl.Entity.Post;
import com.example.Rest1.Impl.DTO.PostRequestTo;
import com.example.Rest1.Impl.DTO.PostResponseTo;
import com.example.Rest1.Impl.CRUD.PostCRUD;
import com.example.Rest1.Mappers.PostMapper;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.List;

@Component
public class PostService implements Service<PostResponseTo, PostRequestTo> {
    @Autowired
    private PostCRUD postCRUD;

    public PostService() {}

    public List<PostResponseTo> getAll() {
        List<Post> postList = postCRUD.getAll();
        List<PostResponseTo> resultList = new ArrayList<>();
        for (int i = 0; i < postList.size(); i++) {
            resultList.add(PostMapper.INSTANCE.PostToPostResponseTo(postList.get(i)));
        }
        return resultList;
    }

    public PostResponseTo update(@Valid PostRequestTo updatingPost) {
        Post post = PostMapper.INSTANCE.PostRequestToToPost(updatingPost);
        boolean result = postCRUD.update(post);
        PostResponseTo responseTo = result ? PostMapper.INSTANCE.PostToPostResponseTo(post) : null;
        return responseTo;
    }

    public PostResponseTo get(@Min(0) long id) {
        return PostMapper.INSTANCE.PostToPostResponseTo(postCRUD.get(id));
    }

    public PostResponseTo delete(@Min(0) long id) {
        return PostMapper.INSTANCE.PostToPostResponseTo(postCRUD.delete(id));
    }

    public PostResponseTo add(@Valid PostRequestTo postRequestTo) {
        Post post = PostMapper.INSTANCE.PostRequestToToPost(postRequestTo);
        return PostMapper.INSTANCE.PostToPostResponseTo(postCRUD.insert(post));
    }
}
