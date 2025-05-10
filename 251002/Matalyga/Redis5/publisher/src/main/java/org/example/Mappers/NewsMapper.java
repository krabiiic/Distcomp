package org.example.Mappers;

import org.example.Impl.DTO.NewsRequestTo;
import org.example.Impl.DTO.NewsResponseTo;
import org.example.Impl.Entity.News;
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