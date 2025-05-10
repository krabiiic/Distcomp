package com.example.JPA2.Mappers;

import com.example.JPA2.Impl.DTO.NewsRequestTo;
import com.example.JPA2.Impl.DTO.NewsResponseTo;
import com.example.JPA2.Impl.Entity.News;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NewsMapper {
    NewsRequestTo NewsToNewsRequestTo(News news);
    @Mapping(source = "user.id", target = "userId")
    NewsResponseTo NewsToNewsResponseTo(News news);
    News NewsResponseToToNews(NewsResponseTo responseTo);
    @Mapping(source = "userId", target = "user.id")
    News NewsRequestToToNews(NewsRequestTo requestTo);
}