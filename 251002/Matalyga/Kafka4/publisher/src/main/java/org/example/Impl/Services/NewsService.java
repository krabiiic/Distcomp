package org.example.Impl.Services;

import org.example.Exceptions.NotFoundException;
import org.example.Exceptions.Violation;
import org.example.Impl.DTO.NewsRequestTo;
import org.example.Impl.DTO.NewsResponseTo;
import org.example.Impl.Entity.News;
import org.example.Impl.Entity.User;
import org.example.Impl.Repository.NewsRepository;
import org.example.Impl.Repository.UserRepository;
import org.example.Mappers.NewsListMapper;
import org.example.Mappers.NewsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.List;

@Component
public class NewsService implements Service<NewsResponseTo, NewsRequestTo> {

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
