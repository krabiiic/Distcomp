package com.dc.service;

import com.dc.exception.ServiceException;
import com.dc.model.blo.News;
import com.dc.model.dto.MessageRequestTo;
import com.dc.repository.NewsRepository;
import com.dc.model.blo.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;

@Service
public class NewsService{

    @Autowired
    private NewsRepository repository;

    @Autowired
    private MessageService messageService;

    public Collection<News> getAll() {
        return repository.findAll();
    }

    public News getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ServiceException("Новость не найдена", 404));
    }

    public News save(News News) {
        if(repository.existsByTitle(News.getTitle()))
            throw new ServiceException("Пользователь не найден", 403);
        News.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        News.setModified(Timestamp.valueOf(LocalDateTime.now()));
        if(!validator(News))
        {
            return null;
        }
        return repository.save(News);
    }

    public News update(News News) {
        News.setModified(Timestamp.valueOf(LocalDateTime.now()));
        if(!validator(News))
        {
            return null;
        }
        return repository.save(News);
    }

    public void delete(Long id) {
        repository.findById(id).orElseThrow(() -> new ServiceException("Нечего удалить", 404));
        Collection<MessageRequestTo> buf = messageService.getAll();
        buf.forEach(x -> {if(x.getNewsId()==id){messageService.delete(x.getId());}});
        repository.deleteById(id);
    }

    private boolean validator(News News){
        if (News.getTitle().length() < 2 || News.getTitle().length() > 64
                || News.getContent().length() < 4 || News.getContent().length() > 2048
        ) {
            return false;
        }
        return true;
    }
}
