package com.example.rest.mapper;

import com.example.rest.dto.PostRequestTo;
import com.example.rest.dto.PostResponseTo;
import com.example.rest.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostMapper {
    Post toEntity(PostRequestTo request);
    PostResponseTo toResponse(Post post);
}