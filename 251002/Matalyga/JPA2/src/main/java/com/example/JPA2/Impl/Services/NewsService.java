package com.example.JPA2.Impl.Services;

import com.example.JPA2.Exceptions.NotFoundException;
import com.example.JPA2.Exceptions.Violation;
import com.example.JPA2.Impl.CRUD.NewsCRUD;
import com.example.JPA2.Impl.DTO.NewsRequestTo;
import com.example.JPA2.Impl.DTO.NewsResponseTo;
import com.example.JPA2.Impl.Entity.News;
import com.example.JPA2.Impl.Entity.User;
import com.example.JPA2.Impl.Repository.NewsRepository;
import com.example.JPA2.Impl.Repository.UserRepository;
import com.example.JPA2.Mappers.NewsListMapper;
import com.example.JPA2.Mappers.NewsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.List;

@Component
public class NewsService implements Service<NewsResponseTo, NewsRequestTo> {
    @Autowired
    private NewsCRUD newsCRUD;

    @Autowired
    private NewsMapper newsMapper;

    @Autowired
    private NewsListMapper newsListMapper;

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private UserRepository userRepository;

    public NewsService() {
    }

    public List<NewsResponseTo> getAll() {
        return newsRepository.getAll()
                .map(newsMapper::NewsToNewsResponseTo)
                .toList();
    }

    public NewsResponseTo update(NewsRequestTo updatingNews) {
        try {
            News news = newsMapper.NewsRequestToToNews(updatingNews);
            User user = userRepository.findById(updatingNews.getUserId())
                    .orElseThrow(() -> new NotFoundException("User not found", 404));
            news.setUser(user);
            return newsMapper.NewsToNewsResponseTo(newsRepository.save(news));
        } catch (DataIntegrityViolationException e) {
            throw new Violation("Title is already taken");
        }

    }

    public NewsResponseTo get(long id) {
        return newsRepository.get(id)
                .map(newsMapper::NewsToNewsResponseTo)
                .orElse(null);
    }

    public NewsResponseTo delete(long id) {
        News news = newsRepository.findById(id).orElseThrow(() -> new NotFoundException("News not found", 404));
        newsRepository.deleteById(id);
        return newsMapper.NewsToNewsResponseTo(news);
    }

    public NewsResponseTo add(NewsRequestTo newsRequestTo) {
        try {
        User user = userRepository.findById(newsRequestTo.getUserId()).orElseThrow(() -> new Violation("User not found"));
        News news = newsMapper.NewsRequestToToNews(newsRequestTo);
        Timestamp now = Timestamp.from(OffsetDateTime.now().toInstant());
        news.setCreated(now);
        news.setModified(now);
        news.setUser(user);
        return newsRepository.add(news).map(newsMapper::NewsToNewsResponseTo).orElseThrow();
        } catch (DataIntegrityViolationException e) {
            throw new Violation("Title is already taken");}
    }

}
