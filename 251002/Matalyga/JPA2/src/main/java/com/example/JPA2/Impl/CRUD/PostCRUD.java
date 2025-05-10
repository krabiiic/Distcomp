package com.example.JPA2.Impl.CRUD;

import com.example.JPA2.Impl.Entity.Post;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PostCRUD implements CRUD<Post> {
    private final Map<Long, Post> PostMemory = new HashMap<>();

    @Override
    public Post get(long id) {
        Post post = PostMemory.get(id);
        if (post != null) {
            post.setId(id);
        }
        return post;
    }

    @Override
    public List<Post> getAll() {
        List<Post> postList = new ArrayList<>();
        for (Long key : PostMemory.keySet()) {
            Post post = PostMemory.get(key);
            post.setId(key);
            postList.add(post);
        }
        return postList;
    }

    @Override
    public Post delete(long id) {
        return PostMemory.remove(id);
    }

    @Override
    public Post insert(Post insertObject) {
        PostMemory.put(insertObject.getId(), insertObject);
        return insertObject;
    }

    @Override
    public boolean update(Post updatingValue) {
        return PostMemory.replace(updatingValue.getId(), PostMemory.get(updatingValue.getId()), updatingValue);
    }
}

