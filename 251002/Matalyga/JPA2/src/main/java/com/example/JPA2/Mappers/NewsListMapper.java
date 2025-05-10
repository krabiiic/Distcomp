package com.example.JPA2.Mappers;

import com.example.JPA2.Impl.DTO.NewsRequestTo;
import com.example.JPA2.Impl.DTO.NewsResponseTo;
import com.example.JPA2.Impl.Entity.News;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = NewsMapper.class)
public interface NewsListMapper {
    List<News> toNewsList(List<NewsRequestTo> list);
    List<NewsResponseTo> toNewsResponseList(List<News> list);
}