package com.example.rv1.service;

import com.example.rv1.dto.NewsDTO;
import com.example.rv1.entity.News;
import com.example.rv1.exception.ExeptionForbidden;
import com.example.rv1.exception.ExceptionBadRequest;
import com.example.rv1.mapper.NewsMapper;
import com.example.rv1.repository.NewsRepository;
import com.example.rv1.repository.EditorRepository;
import com.example.rv1.entity.Editor;
import com.example.rv1.storage.InMemoryStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NewsService {
    private final NewsMapper newsMapper;
    private final NewsRepository newsRepository;
    private final EditorRepository editorRepository;
    public NewsDTO createNews(NewsDTO newsDTO){
        News news = newsMapper.toNews(newsDTO);
        Optional<News> odubl = newsRepository.findNewsByTitle(news.getTitle());
        if(odubl.isPresent()) {
            throw new ExeptionForbidden("403");
        }
        Editor user = editorRepository.findEditorById(news.getEditorId()).orElseThrow(() -> new ExceptionBadRequest("400"));
        newsRepository.save(news);
        NewsDTO dto = newsMapper.toNewsDTO(news);
        return  dto;
    }

    public NewsDTO deleteNews(int id) throws Exception {
        Optional<News> ouser = newsRepository.findNewsById(id);
        News user = ouser.orElseThrow(() -> new ExceptionBadRequest("400"));
        NewsDTO dto = newsMapper.toNewsDTO(user);
        newsRepository.delete(user);
        return  dto;
    }

    public NewsDTO getNews(int id){
        Optional<News> ouser = newsRepository.findNewsById(id);
        News user = ouser.orElseThrow(() -> new ExceptionBadRequest("400"));
        NewsDTO dto = newsMapper.toNewsDTO(user);
        return  dto;
    }

    public List<NewsDTO> getNews(){
        List<News> news = InMemoryStorage.news;
        List<NewsDTO> dtos = newsMapper.toNewsDTOLost(news);
        return  dtos;
    }

    public NewsDTO updateNews(NewsDTO userDTO){
        News tweet = newsMapper.toNews(userDTO);
        newsRepository.save(tweet);
        NewsDTO dto = newsMapper.toNewsDTO(tweet);
        return  dto;
    }

}
