package by.molchan.Publisher.services;


import by.molchan.Publisher.DTOs.Requests.ArticleRequestDTO;
import by.molchan.Publisher.DTOs.Responses.ArticleResponseDTO;
import by.molchan.Publisher.models.Article;
import by.molchan.Publisher.models.Label;
import by.molchan.Publisher.models.Creator;
import by.molchan.Publisher.repositories.ArticlesRepository;
import by.molchan.Publisher.repositories.LabelsRepository;
import by.molchan.Publisher.repositories.CreatorsRepository;
import by.molchan.Publisher.utils.exceptions.NotFoundException;
import by.molchan.Publisher.utils.mappers.ArticlesMapper;
import by.molchan.Publisher.utils.mappers.LabelsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class ArticlesService {
    private final ArticlesRepository articlesRepository;
    private final CreatorsRepository creatorsRepository;
    private final ArticlesMapper articlesMapper;






    @Autowired
    public ArticlesService(ArticlesRepository articlesRepository, CreatorsRepository creatorsRepository,
                         ArticlesMapper articlesMapper) {
        this.articlesRepository = articlesRepository;
        this.creatorsRepository = creatorsRepository;
        this.articlesMapper = articlesMapper;

    }

    private void setCreator(Article article, long creatorId) {
        Creator creator = creatorsRepository.findById(creatorId)
                .orElseThrow(() -> new NotFoundException("Creator with such id does not exist"));
        article.setCreator(creator);
    }

    @Transactional
    public ArticleResponseDTO save(ArticleRequestDTO articleRequestDTO) {
        Article article = articlesMapper.toArticle(articleRequestDTO);
        setCreator(article, articleRequestDTO.getCreatorId());
        if (!articleRequestDTO.getLabels().isEmpty()) {
            article.setLabels(articleRequestDTO.getLabels().stream().map(Label::new).toList());
        }
        article.setCreated(new Date());
        article.setModified(new Date());
        return articlesMapper.toArticleResponse(articlesRepository.save(article));
    }

    @Transactional(readOnly = true)
    public List<ArticleResponseDTO> findAll() {
        return articlesMapper.toArticleResponseList(articlesRepository.findAll());
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "articles", key = "#id")
    public ArticleResponseDTO findById(long id) {
        return articlesMapper.toArticleResponse(
                articlesRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException("Article with such id does not exist")));
    }

    @Transactional
    @CacheEvict(value = "articles", key = "#id")
    public void deleteById(long id) {
        if (!articlesRepository.existsById(id)) {
            throw new NotFoundException("Article not found");
        }
        articlesRepository.deleteById(id);
    }

    @Transactional
    @CacheEvict(value = "articles", key = "#articleRequestDTO.id")
    public ArticleResponseDTO update(ArticleRequestDTO articleRequestDTO) {
        Article article = articlesMapper.toArticle(articleRequestDTO);
        Article oldArticle = articlesRepository.findById(article.getId())
                .orElseThrow(() -> new NotFoundException("Old article not found"));
        Long creatorId = articleRequestDTO.getCreatorId();

        if (creatorId != null) {
            setCreator(article, creatorId);








        }
        article.setCreated(oldArticle.getCreated());
        article.setModified(new Date());
        return articlesMapper.toArticleResponse(articlesRepository.save(article));
    }

    public boolean existsByTitle(String title) {
        return articlesRepository.existsByTitle(title);
    }

    public boolean existsById(Long id) {
        return articlesRepository.existsById(id);
    }
}