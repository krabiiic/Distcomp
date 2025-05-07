package com.example.discussion.service;

import com.example.discussion.dto.NewsRequestTo;
import com.example.discussion.dto.NewsResponseTo;
import com.example.discussion.model.Marker;
import com.example.discussion.model.News;
import com.example.discussion.model.Writer;
import com.example.discussion.repository.MarkerRepository;
import com.example.discussion.repository.NewsRepository;
import com.example.discussion.repository.WriterRepository;
import com.example.discussion.service.mapper.NewsMapper;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NewsService {

    private final NewsRepository newsRepository;
    private  final  WriterRepository writerRepository;
    private  final  MarkerRepository markerRepository;
    private final NewsMapper newsMapper = Mappers.getMapper(NewsMapper.class);

    public NewsService(NewsRepository newsRepository, WriterRepository writerRepository, MarkerRepository markerRepository) {
        this.newsRepository = newsRepository;
        this.writerRepository = writerRepository;
        this.markerRepository = markerRepository;
    }

    public List<NewsResponseTo> findAll() {
        return newsRepository.findAll().stream()
                .map(newsMapper::toDto)
                .collect(Collectors.toList());
    }

    public NewsResponseTo findById(Long id) {
        Optional<News> news = newsRepository.findById(id);
        return news.map(newsMapper::toDto).orElse(null);
    }
    @Transactional
    public NewsResponseTo save(NewsRequestTo newsRequestTo) {
        if (newsRepository.existsByTitle(newsRequestTo.getTitle())) {
            throw new EntityExistsException("A news with the same title already exists.");
        }
        Long writerId = newsRequestTo.getWriterId();
        Optional<Writer> writer = writerRepository.findById(writerId);
        if (!writer.isPresent()) {
            throw new EntityNotFoundException("Author not found with id " + writerId);
        }
        News news = newsMapper.toEntity(newsRequestTo);
        String[] markers = newsRequestTo.getMarkers();
        if(markers.length != 0){
            for(String markerName : markers){
                Optional<Marker> marker = markerRepository.findByName(markerName);
                if(news.getMarkers() == null){
                    news.setMarkers(new HashSet<>());
                }
                if(marker.isPresent()){
                    news.getMarkers().add(marker.get());
                }
                else{
                    Marker newMarker = new Marker();
                    newMarker.setName(markerName);
                    Marker savedMarker = markerRepository.save(newMarker);
                    news.getMarkers().add(savedMarker);
                }
            }
        }
        news.setWriter(writer.get());
        LocalDateTime currentDate = LocalDateTime.now();
        news.setCreated(currentDate);
        news.setModified(currentDate);
        News savedNews = newsRepository.save(news);
        System.out.println(savedNews.toString());
        return newsMapper.toDto(savedNews);
    }


    public NewsResponseTo update(NewsRequestTo newsRequestTo) {
        News existingNews = newsRepository.findById(newsRequestTo.getId()).orElseThrow(() -> new RuntimeException("News not found"));
        newsMapper.updateEntityFromDto(newsRequestTo, existingNews);
        News updatedNews = newsRepository.save(existingNews);
        return newsMapper.toDto(updatedNews);
    }

    public void deleteById(Long id) {
        if (!newsRepository.existsById(id)) {
            throw new EntityNotFoundException("News not found with id " + id);
        }
        newsRepository.deleteById(id);
    }
}