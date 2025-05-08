package com.example.Rest1.Mappers;

import com.example.Rest1.Impl.Entity.News;
import com.example.Rest1.Impl.DTO.NewsRequestTo;
import com.example.Rest1.Impl.DTO.NewsResponseTo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface NewsMapper {
    NewsMapper INSTANCE = Mappers.getMapper(NewsMapper.class);
    NewsRequestTo NewsToNewsRequestTo(News news);
    NewsResponseTo NewsToNewsResponseTo(News news);
    News NewsResponseToToNews(NewsResponseTo responseTo);
    News NewsRequestToToNews(NewsRequestTo requestTo);
}