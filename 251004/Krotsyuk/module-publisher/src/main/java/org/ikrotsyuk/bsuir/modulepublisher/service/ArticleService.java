package org.ikrotsyuk.bsuir.modulepublisher.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.ikrotsyuk.bsuir.modulepublisher.dto.request.ArticleRequestDTO;
import org.ikrotsyuk.bsuir.modulepublisher.dto.request.StickerRequestDTO;
import org.ikrotsyuk.bsuir.modulepublisher.dto.response.ArticleResponseDTO;
import org.ikrotsyuk.bsuir.modulepublisher.entity.ArticleEntity;
import org.ikrotsyuk.bsuir.modulepublisher.entity.StickerEntity;
import org.ikrotsyuk.bsuir.modulepublisher.exception.JSONConverter;
import org.ikrotsyuk.bsuir.modulepublisher.exception.exceptions.ArticleWithSameTitleFoundException;
import org.ikrotsyuk.bsuir.modulepublisher.exception.exceptions.NoWriterWithArticleWriterIdFound;
import org.ikrotsyuk.bsuir.modulepublisher.exception.exceptions.NotFoundException;
import org.ikrotsyuk.bsuir.modulepublisher.mapper.ArticleMapper;
import org.ikrotsyuk.bsuir.modulepublisher.mapper.StickerMapper;
import org.ikrotsyuk.bsuir.modulepublisher.repository.ArticleRepository;
import org.ikrotsyuk.bsuir.modulepublisher.repository.StickerRepository;
import org.ikrotsyuk.bsuir.modulepublisher.repository.WriterRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ArticleService {
    private final ArticleMapper articleMapper;
    private final StickerMapper stickerMapper;
    private final ArticleRepository articleRepository;
    private final WriterRepository writerRepository;
    private final StickerRepository stickerRepository;
    private final JSONConverter jsonConverter;

    @Cacheable(value = "articles")
    @Transactional(readOnly = true)
    public List<ArticleResponseDTO> getArticles(){
        List<ArticleEntity> articleEntityList = articleRepository.findAll();
        if(articleEntityList.isEmpty())
            return Collections.emptyList();
        else
            return articleMapper.toDTOList(articleEntityList);
    }

    @Cacheable(value = "articles", key = "#id")
    @Transactional(readOnly = true)
    public ArticleResponseDTO getArticleById(Long id){
        Optional<ArticleEntity> optionalArticleEntity = articleRepository.findById(id);
        return optionalArticleEntity.map(articleMapper::toDTO).orElse(null);
    }

    @CacheEvict(value = "articles", allEntries = true)
    @Transactional
    public ArticleResponseDTO addArticle(ArticleRequestDTO articleRequestDTO){
        Optional<ArticleEntity> optionalArticleEntity = articleRepository.findByTitle(articleRequestDTO.getTitle());
        if(optionalArticleEntity.isEmpty()) {
            if(writerRepository.existsById(articleRequestDTO.getWriterId())) {
                ArticleEntity articleEntity = articleMapper.toEntity(articleRequestDTO);
                LocalDateTime localDateTime = LocalDateTime.now();
                articleEntity.setCreatedAt(localDateTime);
                articleEntity.setModifiedAt(localDateTime);

                List<StickerRequestDTO> stickerList = articleRequestDTO.getStickers();
                if(stickerList != null && !stickerList.isEmpty()){
                    List<StickerEntity> stickerEntityList = stickerMapper.toEntityList(stickerList);
                    stickerRepository.saveAll(stickerEntityList);
                    articleEntity.assignStickers(stickerEntityList);
                }

                return articleMapper.toDTO(articleRepository.save(articleEntity));
            } else
                try{
                    throw new NoWriterWithArticleWriterIdFound(jsonConverter.convertObjectToJSON(articleRequestDTO));
                }catch (JsonProcessingException ex){
                    return null;
                }

        } else
            try{
                throw new ArticleWithSameTitleFoundException(jsonConverter.convertObjectToJSON(articleRequestDTO));
            }catch (JsonProcessingException ex){
                return articleMapper.toDTO(optionalArticleEntity.get());
            }
    }

    @CacheEvict(value = "articles", key = "#id")
    @Transactional
    public ArticleResponseDTO deleteArticle(Long id){
        Optional<ArticleEntity> optionalArticleEntity = articleRepository.findById(id);
        if(optionalArticleEntity.isPresent()){
            articleRepository.deleteById(id);
            return articleMapper.toDTO(optionalArticleEntity.get());
        } else
            throw new NotFoundException();
    }

    @CacheEvict(value = "articles", key = "#id")
    @Transactional
    public ArticleResponseDTO updateArticle(Long id, ArticleRequestDTO articleRequestDTO) {
        Optional<ArticleEntity> optionalArticleEntity = articleRepository.findById(id);
        if(optionalArticleEntity.isPresent()){
            ArticleEntity articleEntity = optionalArticleEntity.get();
            articleEntity.getWriter().setId(articleRequestDTO.getWriterId());
            articleEntity.setTitle(articleRequestDTO.getTitle());
            articleEntity.setContent(articleRequestDTO.getContent());
            return articleMapper.toDTO(articleRepository.save(articleEntity));
        } else
            throw new NotFoundException();
    }
}
