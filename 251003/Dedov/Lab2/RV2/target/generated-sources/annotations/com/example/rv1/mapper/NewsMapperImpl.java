package com.example.rv1.mapper;

import com.example.rv1.dto.NewsDTO;
import com.example.rv1.entity.News;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-02T18:43:27+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.2 (Oracle Corporation)"
)
@Component
public class NewsMapperImpl implements NewsMapper {

    @Override
    public News toNews(NewsDTO newsDTO) {
        if ( newsDTO == null ) {
            return null;
        }

        News news = new News();

        news.setId( newsDTO.getId() );
        news.setEditorId( newsDTO.getEditorId() );
        news.setTitle( newsDTO.getTitle() );
        news.setContent( newsDTO.getContent() );

        return news;
    }

    @Override
    public NewsDTO toNewsDTO(News news) {
        if ( news == null ) {
            return null;
        }

        NewsDTO newsDTO = new NewsDTO();

        newsDTO.setId( news.getId() );
        newsDTO.setEditorId( news.getEditorId() );
        newsDTO.setTitle( news.getTitle() );
        newsDTO.setContent( news.getContent() );

        return newsDTO;
    }

    @Override
    public List<News> toNewsList(List<NewsDTO> newsDTOS) {
        if ( newsDTOS == null ) {
            return null;
        }

        List<News> list = new ArrayList<News>( newsDTOS.size() );
        for ( NewsDTO newsDTO : newsDTOS ) {
            list.add( toNews( newsDTO ) );
        }

        return list;
    }

    @Override
    public List<NewsDTO> toNewsDTOLost(List<News> news) {
        if ( news == null ) {
            return null;
        }

        List<NewsDTO> list = new ArrayList<NewsDTO>( news.size() );
        for ( News news1 : news ) {
            list.add( toNewsDTO( news1 ) );
        }

        return list;
    }
}
