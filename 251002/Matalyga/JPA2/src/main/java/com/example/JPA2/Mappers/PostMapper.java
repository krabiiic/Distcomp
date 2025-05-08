package com.example.JPA2.Mappers;

import com.example.JPA2.Impl.DTO.PostRequestTo;
import com.example.JPA2.Impl.DTO.PostResponseTo;
import com.example.JPA2.Impl.Entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PostMapper {
    //PostMapper INSTANCE = Mappers.getMapper( PostMapper.class );
    Post PostResponseToToPost(PostResponseTo postResponseTo);
    @Mapping(source = "newsId", target = "news.id")
    Post PostRequestToToPost(PostRequestTo postRequestTo);
    @Mapping(source = "news.id", target = "newsId")
    PostResponseTo PostToPostResponseTo(Post post);
    PostRequestTo PostToPostRequestTo(Post post);
}
