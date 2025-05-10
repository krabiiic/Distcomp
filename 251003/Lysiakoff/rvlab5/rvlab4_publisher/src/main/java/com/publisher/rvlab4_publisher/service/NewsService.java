package com.publisher.rvlab4_publisher.service;

import com.publisher.rvlab4_publisher.dto.NewsRequestTo;
import com.publisher.rvlab4_publisher.dto.NewsResponseTo;
import com.publisher.rvlab4_publisher.entity.Mark;
import com.publisher.rvlab4_publisher.entity.News;
import com.publisher.rvlab4_publisher.entity.NewsMark;
import com.publisher.rvlab4_publisher.repository.MarkRepository;
import com.publisher.rvlab4_publisher.repository.NewsMarkRepository;
import com.publisher.rvlab4_publisher.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewsService {
    private final NewsRepository newsRepository;
    private final MarkRepository markRepository;
    private final NewsMarkRepository newsMarkRepository;

    public List<NewsResponseTo> getAllNews() {
        return newsRepository.findAll().stream()
                .map(news -> new NewsResponseTo(
                        news.getId(),
                        news.getTitle(),
                        news.getContent(),
                        news.getAuthor_id(),
                        news.getMark_ids(),
                        news.getMessage_ids()))
                .collect(Collectors.toList()
                );
    }

    public NewsResponseTo getNewsById(Long id) {
        return newsRepository.findById(id)
                .map(news -> new NewsResponseTo(
                        news.getId(),
                        news.getTitle(),
                        news.getContent(),
                        news.getAuthor_id(),
                        news.getMark_ids(),
                        news.getMessage_ids()))
                .orElseThrow(() -> new NoSuchElementException("News not found")
                );
    }

    public NewsResponseTo createNews(NewsRequestTo request) {
        News news = new News();

        Long newsID = System.currentTimeMillis();

        news.setId(newsID);
        news.setTitle(request.getTitle());
        news.setContent(request.getContent());
        news.setAuthor_id(request.getAuthorId());

        News savedNews = newsRepository.save(news);

        List<String> markStrings = request.getMarks();
        if (markStrings != null) {
            for (String strMark : markStrings) {
                Mark mark = new Mark();
                mark.setName(strMark);

                Mark savedMark = markRepository.save(mark);

                NewsMark newsMark = new NewsMark();
                newsMark.setNews_id(savedNews.getId());
                newsMark.setMark_id(savedMark.getId());

                NewsMark newsMark1 = newsMarkRepository.save(newsMark);
            }
        }

        return new NewsResponseTo(
                savedNews.getId(),
                savedNews.getTitle(),
                savedNews.getContent(),
                savedNews.getAuthor_id(),
                news.getMark_ids(),
                news.getMessage_ids()
        );

    }

    public NewsResponseTo updateNews(Long id, NewsRequestTo request) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("News not found"));

        if (request.getTitle() == null || request.getTitle().length() < 3) {
            throw new IllegalArgumentException("Title must be at least 3 characters long");
        }

        news.setTitle(request.getTitle());
        news.setContent(request.getContent());
        news.setAuthor_id(request.getAuthorId());

        newsRepository.save(news);
        return new NewsResponseTo(
                id,
                news.getTitle(),
                news.getContent(),
                news.getAuthor_id(),
                news.getMark_ids(),
                news.getMessage_ids()
        );
    }

    public void deleteNews(Long id) {
        News news = newsRepository.findById(id).orElseThrow(() -> new NoSuchElementException("News no found"));
        newsRepository.deleteById(id);
    }
}
