package com.example.Rest1.Impl.Services;

import com.example.Rest1.Impl.Entity.News;
import com.example.Rest1.Impl.DTO.NewsRequestTo;
import com.example.Rest1.Impl.DTO.NewsResponseTo;
import com.example.Rest1.Impl.CRUD.NewsCRUD;
import com.example.Rest1.Mappers.NewsMapper;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.List;

@Component
public class NewsService implements Service<NewsResponseTo, NewsRequestTo> {
    @Autowired
    private NewsCRUD newsCRUD;

    public NewsService() {

    }

    public List<NewsResponseTo> getAll() {
        List<News> newsList = newsCRUD.getAll();
        List<NewsResponseTo> resultList = new ArrayList<>();
        for (int i = 0; i < newsList.size(); i++) {
            resultList.add(NewsMapper.INSTANCE.NewsToNewsResponseTo(newsList.get(i)));
        }
        return resultList;
    }

    public NewsResponseTo update(@Valid NewsRequestTo updatingNews) {
        News news = NewsMapper.INSTANCE.NewsRequestToToNews(updatingNews);
        if (validateNews(news)) {
            boolean result = newsCRUD.update(news);
            NewsResponseTo responseTo = result ? NewsMapper.INSTANCE.NewsToNewsResponseTo(news) : null;
            return responseTo;
        }
        return new NewsResponseTo();
    }

    public NewsResponseTo get(@Min(0) long id) {
        return NewsMapper.INSTANCE.NewsToNewsResponseTo(newsCRUD.get(id));
    }

    public NewsResponseTo delete(@Min(0) long id) {
        return NewsMapper.INSTANCE.NewsToNewsResponseTo(newsCRUD.delete(id));
    }

    public NewsResponseTo add(@Valid NewsRequestTo newsRequestTo) {
        News news = NewsMapper.INSTANCE.NewsRequestToToNews(newsRequestTo);
        return NewsMapper.INSTANCE.NewsToNewsResponseTo(newsCRUD.insert(news));
    }

    private boolean validateNews(News news) {
        String title = news.getTitle();
        String content = news.getContent();

        if ((content.length() >= 4 && content.length() <= 2048) && (title.length() >= 2 && title.length() <= 64)) {
            return true;
        }
        return false;
    }
}
