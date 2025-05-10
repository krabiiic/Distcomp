package com.example.rv1.mapper;

import com.example.rv1.dto.NewsDTO;
import com.example.rv1.entity.News;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NewsMapper {
   News toNews(NewsDTO newsDTO);
   NewsDTO toNewsDTO(News news);
   List<News> toNewsList(List<NewsDTO> newsDTOS);
   List<NewsDTO> toNewsDTOLost(List<News> news);
}
