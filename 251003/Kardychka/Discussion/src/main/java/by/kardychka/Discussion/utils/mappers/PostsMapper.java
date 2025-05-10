package by.kardychka.Discussion.utils.mappers;


import by.kardychka.Discussion.DTOs.Requests.PostRequestDTO;
import by.kardychka.Discussion.DTOs.Responses.PostResponseDTO;
import by.kardychka.Discussion.models.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PostsMapper {

    @Mapping(target = "id", expression = "java(post.getKey().getId())")
    @Mapping(target = "newsId", expression = "java(post.getKey().getNewsId())")
    PostResponseDTO toPostResponse(Post post);

    List<PostResponseDTO> toPostResponseList(Iterable<Post> posts);
    @Mapping(target = "key", expression = "java(new Post.PostKey(postRequestDTO.getNewsId()))")
    Post toPost(PostRequestDTO postRequestDTO);
}
