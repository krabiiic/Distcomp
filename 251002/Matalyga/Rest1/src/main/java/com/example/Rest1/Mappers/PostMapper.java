package com.example.Rest1.Mappers;

import com.example.Rest1.Impl.Entity.Post;
import com.example.Rest1.Impl.DTO.PostRequestTo;
import com.example.Rest1.Impl.DTO.PostResponseTo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PostMapper {
    PostMapper INSTANCE = Mappers.getMapper( PostMapper.class );
    Post PostResponseToToPost(PostResponseTo postResponseTo);
    Post PostRequestToToPost(PostRequestTo postRequestTo);
    PostResponseTo PostToPostResponseTo(Post post);
    PostRequestTo PostToPostRequestTo(Post post);
}
