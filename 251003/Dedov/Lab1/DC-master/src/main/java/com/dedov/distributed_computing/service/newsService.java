package com.dedov.distributed_computing.service;

import com.dedov.distributed_computing.dao.InMemorynewsDao;
import com.dedov.distributed_computing.dto.request.newsRequestTo;
import com.dedov.distributed_computing.dto.response.newsResponseTo;
import com.dedov.distributed_computing.exception.EntityNotFoundException;
import com.dedov.distributed_computing.model.news;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class newsService {

    private final ModelMapper modelMapper;
    private final InMemorynewsDao newsDao;

    @Autowired
    public newsService(ModelMapper modelMapper, InMemorynewsDao newsDao) {
        this.modelMapper = modelMapper;
        this.newsDao = newsDao;
    }

    public List<newsResponseTo> findAll() {
        return newsDao.findAll()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public newsResponseTo findById(long id) throws EntityNotFoundException {
        news news = newsDao.findById(id).orElseThrow(() -> new EntityNotFoundException("This news doesn't exist."));
        return convertToResponse(news);
    }
    

    public newsResponseTo save(newsRequestTo newsRequestTo) {
        news news = convertTonews(newsRequestTo);
        news.setCreated(new Date());
        news.setModified(news.getCreated());
        newsDao.save(news);
        return convertToResponse(news);
    }

    public newsResponseTo update(newsRequestTo newsRequestTo) throws EntityNotFoundException{
        news existingnews = newsDao.findById(newsRequestTo.getId()).orElseThrow(() -> new EntityNotFoundException("This news doesn't exist."));

        news updatednews = convertTonews(newsRequestTo);
        updatednews.setId(newsRequestTo.getId());
        updatednews.setCreated(existingnews.getCreated());
        updatednews.setModified(new Date());

        newsDao.save(updatednews);

        return convertToResponse(updatednews);
    }

    public newsResponseTo partialUpdate(newsRequestTo newsRequestTo) throws EntityNotFoundException{
        news news = newsDao.findById(newsRequestTo.getId()).orElseThrow(() -> new EntityNotFoundException("This news doesn't exist."));

        if (newsRequestTo.getContent() != null) {
            news.setContent(newsRequestTo.getContent());
        }
        if (newsRequestTo.getTitle() != null) {
            news.setTitle(newsRequestTo.getTitle());
        }
        if (newsRequestTo.geteditorId() != 0) {
            news.seteditorId(newsRequestTo.geteditorId());
        }

        news.setModified(new Date());
        newsDao.save(news);

        return convertToResponse(news);
    }

    public void delete(long id) throws EntityNotFoundException {
        newsDao.findById(id).orElseThrow(() -> new EntityNotFoundException("news doesn't exist."));
        newsDao.deleteById(id);
    }

    private news convertTonews(newsRequestTo newsRequestTo) {
        return this.modelMapper.map(newsRequestTo, news.class);
    }

    private newsResponseTo convertToResponse(news news) {
        return this.modelMapper.map(news, newsResponseTo.class);
    }
}
