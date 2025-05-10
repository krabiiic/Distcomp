package com.example.JPA2.Mappers;

import com.example.JPA2.Impl.DTO.PostRequestTo;
import com.example.JPA2.Impl.DTO.PostResponseTo;
import com.example.JPA2.Impl.Entity.Post;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = PostMapper.class)
public interface PostListMapper {
    List<Post> toPostList(List<PostRequestTo> list);
    List<PostResponseTo> toPostResponseList(List<Post> list);
}
