package com.bsuir.discussion.utils.mappers;

import com.bsuir.discussion.dto.requests.PostRequestDTO;
import com.bsuir.discussion.dto.responses.PostResponseDTO;
import com.bsuir.discussion.models.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PostsMapper {
    @Mapping(target = "id", expression = "java(post.getKey().getId())")
    @Mapping(target = "topicId", expression = "java(post.getKey().getTopicId())")
    PostResponseDTO toPostResponse(Post post);

    List<PostResponseDTO> toPostResponseList(Iterable<Post> posts);
    @Mapping(target = "key", expression = "java(new Post.PostKey(postRequestDTO.getTopicId()))")
    Post toPost(PostRequestDTO postRequestDTO);
}
