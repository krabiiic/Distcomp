package com.example.rest.mapper;

import com.example.rest.dto.PostRequestTo;
import com.example.rest.dto.PostResponseTo;
import com.example.rest.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostMapper {
    @Mapping(target = "id", expression = "java(postRequestTo.getId())")
    @Mapping(target = "topicId", expression = "java(postRequestTo.getTopicId())")
    @Mapping(target = "content", expression = "java(postRequestTo.getContent())")
    PostResponseTo toPostResponse(PostRequestTo  postRequestTo);
}