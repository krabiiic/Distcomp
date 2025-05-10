package org.ikrotsyuk.bsuir.modulepublisher.service;

import by.ikrotsyuk.bsuir.kafkacommunicationparts.events.KafkaCreateReactionEvent;
import by.ikrotsyuk.bsuir.kafkacommunicationparts.events.KafkaDeleteReactionEvent;
import by.ikrotsyuk.bsuir.kafkacommunicationparts.events.KafkaGetReactionEvent;
import by.ikrotsyuk.bsuir.kafkacommunicationparts.events.KafkaGetReactionsEvent;
import by.ikrotsyuk.bsuir.kafkacommunicationparts.events.KafkaUpdateReactionEvent;
import by.ikrotsyuk.bsuir.kafkacommunicationparts.events.dto.KafkaReactionListResponseDTO;
import by.ikrotsyuk.bsuir.kafkacommunicationparts.events.dto.KafkaReactionResponseDTO;
import by.ikrotsyuk.bsuir.kafkacommunicationparts.events.dto.KafkaReactionResponseEventDTO;
import lombok.RequiredArgsConstructor;
import org.ikrotsyuk.bsuir.modulepublisher.dto.request.ReactionRequestDTO;
import org.ikrotsyuk.bsuir.modulepublisher.dto.response.ReactionResponseDTO;
import org.ikrotsyuk.bsuir.modulepublisher.exception.exceptions.NotFoundException;
import org.ikrotsyuk.bsuir.modulepublisher.kafka.KafkaProducer;
import org.ikrotsyuk.bsuir.modulepublisher.mapper.ReactionMapper;
import org.ikrotsyuk.bsuir.modulepublisher.repository.ArticleRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ReactionService {
    private final ArticleRepository articleRepository;
    private final KafkaProducer kafkaProducer;
    private final ReactionMapper reactionMapper;

    @Cacheable(value = "reactions")
    public List<ReactionResponseDTO> getReactions(){
        String eventId = getUUID();
        KafkaReactionListResponseDTO kafkaReactionListResponseDTO = (KafkaReactionListResponseDTO) kafkaProducer.sendRequest(new KafkaGetReactionsEvent(eventId), eventId);
        List<KafkaReactionResponseEventDTO> responseDTOList =  kafkaReactionListResponseDTO.getReactionDTO();
        return reactionMapper.toResponseDTOList(responseDTOList);
    }

    @Cacheable(value = "reactions", key = "#id")
    public ReactionResponseDTO getReactionById(Long id){
        String eventId = getUUID();
        KafkaReactionResponseDTO kafkaReactionResponseDTO = (KafkaReactionResponseDTO)kafkaProducer.sendRequest(new KafkaGetReactionEvent(
                eventId, id), eventId
        );
        return reactionMapper.toResponseDTO(kafkaReactionResponseDTO.getReactionDTO());
    }

    @CacheEvict(value = "reactions", allEntries = true)
    public ReactionResponseDTO addReaction(ReactionRequestDTO reactionRequestDTO){
        if(articleRepository.existsById(reactionRequestDTO.getArticleId())) {
            String eventId = getUUID() + reactionRequestDTO.getArticleId();
            KafkaReactionResponseDTO kafkaReactionResponseDTO = (KafkaReactionResponseDTO) kafkaProducer.sendRequest(new KafkaCreateReactionEvent(
                eventId, reactionMapper.toReactionDTO(reactionRequestDTO)
                ), eventId
            );
            return reactionMapper.toResponseDTO(kafkaReactionResponseDTO.getReactionDTO());
        } else
            throw new NotFoundException();
    }

    @CacheEvict(value = "reactions", key = "#id")
    public ReactionResponseDTO deleteReaction(Long id){
        String eventId = getUUID();
        KafkaReactionResponseDTO kafkaReactionResponseDTO = (KafkaReactionResponseDTO) kafkaProducer.sendRequest(new KafkaDeleteReactionEvent(
                eventId, id
        ), eventId);
        return reactionMapper.toResponseDTO(kafkaReactionResponseDTO.getReactionDTO());
    }

    @CacheEvict(value = "reactions", key = "#id")
    public ReactionResponseDTO updateReaction(Long id, ReactionRequestDTO reactionRequestDTO){
        if(articleRepository.existsById(reactionRequestDTO.getArticleId())) {
            String eventId = getUUID() + reactionRequestDTO.getArticleId();
            KafkaReactionResponseDTO kafkaReactionResponseDTO = (KafkaReactionResponseDTO) kafkaProducer.sendRequest(new KafkaUpdateReactionEvent(
                    eventId, id, reactionMapper.toReactionDTO(reactionRequestDTO)
            ), eventId);
            return reactionMapper.toResponseDTO(kafkaReactionResponseDTO.getReactionDTO());
        } else
            throw new NotFoundException();
    }

    public String getUUID(){
        return UUID.randomUUID().toString();
    }
}
