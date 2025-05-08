package org.example.Mappers;

import org.example.Impl.DTO.NewsRequestTo;
import org.example.Impl.DTO.NewsResponseTo;
import org.example.Impl.Entity.News;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = NewsMapper.class)
public interface NewsListMapper {
    List<News> toNewsList(List<NewsRequestTo> list);
    List<NewsResponseTo> toNewsResponseList(List<News> list);
}