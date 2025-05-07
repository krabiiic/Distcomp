package by.bsuir.distcomp.service;

import by.bsuir.distcomp.dto.mapper.ArticleMapper;
import by.bsuir.distcomp.dto.request.ArticleRequestTo;
import by.bsuir.distcomp.dto.response.ArticleResponseTo;
import by.bsuir.distcomp.entity.Article;
import by.bsuir.distcomp.entity.Marker;
import by.bsuir.distcomp.repository.ArticleRepository;
import by.bsuir.distcomp.repository.MarkerRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final MarkerRepository markerRepository;

    private final ArticleMapper articleMapper;

    public ArticleService(ArticleRepository articleRepository, ArticleMapper articleMapper, MarkerRepository markerRepository) {
        this.articleRepository = articleRepository;
        this.articleMapper = articleMapper;
        this.markerRepository = markerRepository;
    }

    @Cacheable(value = "articles", key = "'all'")
    public List<ArticleResponseTo> getAllArticles() {
        return articleRepository.findAll().stream().map(articleMapper::toDto).toList();
    }

    @Cacheable(value = "articles", key = "#id")
    public ArticleResponseTo getArticleById(Long id) {
        return articleRepository.findById(id)
                .map(articleMapper::toDto)
                .orElseThrow(() -> new NoSuchElementException("Article with id: " + id + " not found"));
    }

    @Transactional
    @CacheEvict(value = "articles", key = "'all'")
    public ArticleResponseTo createArticle(ArticleRequestTo articleRequestTo) {
        Article article = articleMapper.toEntity(articleRequestTo);

        markerRepository.saveAll(article.getMarkers());

        return articleMapper.toDto(articleRepository.save(article));
    }

    @Transactional
    @Caching (
            put = @CachePut(value = "articles", key = "#articleRequestTo.id"),
            evict = @CacheEvict(value = "articles", key = "'all'")
    )
    public ArticleResponseTo updateArticle(ArticleRequestTo articleRequestTo) {
        Long id = articleRequestTo.getId();
        articleRepository
                .findById(id)
                .orElseThrow(() -> new NoSuchElementException("Article with id: " + id + " not found"));

        return articleMapper.toDto(articleRepository.save(articleMapper.toEntity(articleRequestTo)));
    }

    @Transactional
    @Caching (
            evict = {
                    @CacheEvict(value = "articles", key = "'all'"),
                    @CacheEvict(value = "articles", key = "#id")
            }
    )
    public void deleteArticle(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Article with id: " + id + " not found"));

        for (Marker marker : article.getMarkers()) {
            markerRepository.deleteById(marker.getId());
        }

        articleRepository.deleteById(id);
    }
    
}
